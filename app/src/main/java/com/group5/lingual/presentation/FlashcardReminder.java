package com.group5.lingual.presentation;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.group5.lingual.R;
import com.group5.lingual.data.AssetLoader;
import com.group5.lingual.logic.AppInitializer;
import com.group5.lingual.logic.FlashcardQueue;
import com.group5.lingual.logic.LanguageList;
import com.group5.lingual.logic.SaveManager;

import java.io.IOException;
import java.util.Calendar;

//Creates a notification to the user about how many flashcards they have due
public class FlashcardReminder extends BroadcastReceiver
{
    private static final int NOTIFICATION_HOUR = 8;   //Hour of the day to notify the user
    private static final String NOTIFICATION_CHANNEL_ID = "fReminder";
    private static final String NOTIFICATION_CHANNEL_NAME = "Flashcard Reminders";

    //Sets an alarm to run this reminder daily
    public static void setReminderAlarm(Context context)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Get the time to schedule the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);

        //Get the intent to launch the reminder
        PendingIntent intent = PendingIntent.getBroadcast(context, 0, new Intent(context, FlashcardReminder.class), 0);

        //Set the alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Firstly, initialize the app to ensure database has been loaded
        AppInitializer.initialize(context);

        //Get the number of due cards
        int dueCards = getTotalDueCards();

        //If any cards are due, notify the user
        if(dueCards > 0)
            sendDueNotification(context, dueCards);

        //Save any pending data
        SaveManager.save();
    }

    //Builds and sends the notification
    private void sendDueNotification(Context context, int cardCount)
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If the OS version is sufficiently new, create a notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW));

        //Create the intent to launch the app when the notification is clicked
        Intent intent = new Intent(context, LanguageListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(context.getResources().getString(R.string.freminder_notification_title));
        builder.setContentText(context.getResources().getQuantityString(R.plurals.freminder_notification_description, cardCount, cardCount));
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);

        //Send the notification
        notificationManager.notify((int)System.currentTimeMillis(), builder.build());
    }

    //Determines the total number of due cards
    private int getTotalDueCards()
    {
        int total = 0;

        //For each language, get the number of due cards
        LanguageList lList = new LanguageList();
        for(int lID : lList.getAllIDs())
            total += new FlashcardQueue(lID).dueCardCount();

        return total;
    }
}