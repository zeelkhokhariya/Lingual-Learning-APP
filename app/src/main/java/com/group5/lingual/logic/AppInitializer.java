package com.group5.lingual.logic;

import android.content.Context;

import com.group5.lingual.data.AssetLoader;
import com.group5.lingual.presentation.FlashcardReminder;

import java.io.IOException;

//Performs necessary actions to initialize the app on startup
public class AppInitializer
{
    public static void initialize(Context context) { initialize(context, false); }

    //If reset is true, also reset the app to its original default state
    public static void initialize(Context context, boolean reset)
    {
        //Load application assets
        try { AssetLoader.copyDatabaseToDevice(context, reset); }
        catch(IOException e) { throw new IllegalStateException("Database not loaded correctly", e); }

        //Set daily reminder about due flashcards
        FlashcardReminder.setReminderAlarm(context);
    }
}
