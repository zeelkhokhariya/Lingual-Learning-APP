package com.group5.lingual.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.lingual.R;
import com.group5.lingual.dso.flashcards.IFlashcard;
import com.group5.lingual.logic.FlashcardQueue;
import com.group5.lingual.logic.IFlashcardQueue;
import com.group5.lingual.logic.LanguageList;

//Allows the user to view and answer flashcards
public class FlashcardActivity extends DataSafeActivity
{
    private IFlashcardQueue queue;      //Queue of flashcards for the active language
    private IFlashcard loadedCard;      //The currently loaded flashcard

    private ActionBar actionBar;        //The ActionBar displayed during the activity

    private TextView questionText;      //The TextView displaying the loaded card's question
    private TextView answerText;        //The TextView displaying the loaded card's answer

    private Button answerWrongButton;   //The button to indicate the loaded card was answered incorrectly
    private Button answerRightButton;   //The button to indicate the loaded card was answered correctly
    private Button revealButton;        //The button that blocks the answer, and is clicked to reveal it

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        //Retrieve the active language ID from the intent, finish activity if invalid
        Intent intent = getIntent();
        int activeLanguageID = intent.getIntExtra("activeLanguageID", -1);
        if(activeLanguageID < 0)
            finish();

        //Get the flashcard queue for this language
        queue = new FlashcardQueue(activeLanguageID);

        //Find the action bar and set it to display the active language name
        actionBar = getSupportActionBar();
        actionBar.setTitle(new LanguageList().getLanguage(activeLanguageID).getName());

        //Find question and answer display
        questionText = findViewById(R.id.flash_text_question);
        answerText = findViewById(R.id.flash_text_answer);

        //Find buttons
        answerWrongButton = findViewById(R.id.flash_button_wrong);
        answerRightButton = findViewById(R.id.flash_button_right);
        revealButton = findViewById(R.id.flash_button_reveal);

        //Set button listeners
        answerWrongButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { answerCard(false); } });
        answerRightButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { answerCard(true); } });
        revealButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showAnswer(true); } });

        //Load the first card
        loadFlashcard(queue.getNextCard());
    }

    //Display a menu item to lookup a user-highlighted word
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dictionary_lookup, menu);
        return true;
    }

    //Handle when the user clicks the menu item to lookup a user-highlighted word
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menu_item_dictionary_lookup)
        {
            Intent lookupIntent = DictionaryLookup.createLookupIntent(findViewById(R.id.flash_layout_root),
                    new LanguageList().getLanguage(loadedCard.getLanguageID()));
            if(lookupIntent != null)
                startActivity(lookupIntent);
            else //If no selected text found, notify the user
                Toast.makeText(FlashcardActivity.this, getResources().getString(R.string.no_text_selected), Toast.LENGTH_SHORT).show();

            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    //Answer the loaded card, correctly or incorrectly, and load the next card
    private void answerCard(boolean correct)
    {
        queue.answerCard(loadedCard, correct);
        loadFlashcard(queue.getNextCard());
    }

    //Set whether to show or hide the flashcard answer
    private void showAnswer(boolean show)
    {
        revealButton.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    //Load a given flashcard, and display its question and answer, and hide the answer
    //Finish the activity if the supplied flashcard is null
    private void loadFlashcard(IFlashcard f)
    {
        loadedCard = f;

        if(loadedCard != null)
        {
            showAnswer(false);
            questionText.setText(loadedCard.getQuestion());
            answerText.setText(loadedCard.getAnswer());

            //Display the number of remaining flashcards in the action bar subtitle
            int cardsRemaining = queue.dueCardCount();
            actionBar.setSubtitle(getResources().getQuantityString(R.plurals.flash_cards_remaining,
                    cardsRemaining, cardsRemaining));
        }
        else
            finish();
    }
}