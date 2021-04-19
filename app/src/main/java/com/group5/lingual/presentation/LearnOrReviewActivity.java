package com.group5.lingual.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.lingual.R;
import com.group5.lingual.dso.languages.ILanguage;
import com.group5.lingual.logic.FlashcardQueue;
import com.group5.lingual.logic.IFlashcardQueue;
import com.group5.lingual.logic.ILanguageList;
import com.group5.lingual.logic.ILessonList;
import com.group5.lingual.logic.LanguageList;
import com.group5.lingual.logic.LessonList;

//Allows the user to choose between reviewing flashcards or learning lessons
public class LearnOrReviewActivity extends DataSafeActivity
{
    private ILanguageList languageList;      //List of languages available in the app
    private ILessonList lessonList;          //List of lessons available in the app
    private IFlashcardQueue flashcardQueue;  //Flashcard queue for the current active language

    private int activeLanguageID;           //ID of the currently chosen language

    private Button learnButton;             //Button to take the user to lessons
    private Button reviewButton;            //Button to take the user to flashcard review

    private TextView lessonsAvailable;      //Text indicating how many lessons are available to study
    private TextView cardsAvailable;        //Text indicating how many flashcards are available to review

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_or_review);

        //Retrieve the active language ID from the intent, finish activity if invalid
        Intent intent = getIntent();
        activeLanguageID = intent.getIntExtra("activeLanguageID", -1);
        if(activeLanguageID < 0)
            finish();

        //Get the language list, lesson list and the flashcard queue for the current active language
        languageList = new LanguageList();
        lessonList = new LessonList(activeLanguageID);
        flashcardQueue = new FlashcardQueue(activeLanguageID);

        //Set action bar to display the active language name
        setTitle(languageList.getLanguage(activeLanguageID).getName());

        //Find buttons
        learnButton = findViewById(R.id.lor_button_learn);
        reviewButton = findViewById(R.id.lor_button_review);

        //Find lesson and card counters
        lessonsAvailable = findViewById(R.id.lor_text_lessons);
        cardsAvailable = findViewById(R.id.lor_text_cards);

        updateAvailable(); //Update the lesson and card counts before the user sees anything

        //Set button listeners
        learnButton.setOnClickListener(v ->
        {
            Intent lessonIntent = new Intent(LearnOrReviewActivity.this, LessonListActivity.class);
            lessonIntent.putExtra("activeLanguageID", activeLanguageID);
            startActivity(lessonIntent);
        });
        reviewButton.setOnClickListener(v ->
        {
            Intent flashcardIntent = new Intent(LearnOrReviewActivity.this, FlashcardActivity.class);
            flashcardIntent.putExtra("activeLanguageID", activeLanguageID);
            startActivity(flashcardIntent);
        });
    }

    //Display a menu item to view the credits for this language's course content
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_learn_or_review, menu);
        return true;
    }

    //Handle when the user clicks the menu item to view content credits
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menu_item_credits)
        {
            showContentCredits();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        updateAvailable(); //Update the available lesson and card counts
    }

    //Updates the number of available lessons and cards,
    //and enables or disables the buttons accordingly
    private void updateAvailable()
    {
        //Get the number of available lessons and flashcards, and the remaining lesson duration, for the active language
        int lessonCount = lessonList.unlockedLessonCount();
        int minutesRemaining = lessonList.remainingDuration();
        int cardCount = flashcardQueue.dueCardCount();

        //Display these counts
        lessonsAvailable.setText(
                getResources().getQuantityString(R.plurals.lor_lessons_unlocked, lessonCount, lessonCount) +
                (minutesRemaining > 0 ? ("\n" + getResources().getQuantityString(R.plurals.lor_minutes_remaining, minutesRemaining, minutesRemaining)) : ""));
        cardsAvailable.setText(getResources().getQuantityString(R.plurals.lor_cards_due, cardCount, cardCount));

        //If no lessons or cards are available, disable their respective buttons
        learnButton.setEnabled(lessonCount > 0);
        reviewButton.setEnabled(cardCount > 0);
    }

    //Show a dialog containing the language's course content credits
    private void showContentCredits()
    {
        ILanguage activeLanguage = languageList.getLanguage(activeLanguageID);

        //Create the dialog
        AlertDialog creditsDialog = new AlertDialog.Builder(LearnOrReviewActivity.this).create();

        //Set the dialog title and message
        creditsDialog.setTitle(activeLanguage.getName() + " Content Sources");
        creditsDialog.setMessage(activeLanguage.getCredits());

        //Add a button to dismiss the dialog
        creditsDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                (dialogInterface, i) -> creditsDialog.dismiss());

        creditsDialog.show();
    }
}