package com.group5.lingual.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.lingual.R;
import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.ILessonModule;
import com.group5.lingual.logic.ILessonList;
import com.group5.lingual.logic.LanguageList;
import com.group5.lingual.logic.LessonList;
import com.group5.lingual.presentation.lessonfragments.LessonFragmentFactory;

//Allows the user to view a loaded lesson
//Currently a placeholder to replaced with a full lesson activity later on
public class LessonActivity extends DataSafeActivity
{
    private ILesson loadedLesson;   //The currently loaded lesson

    private Button completeButton;  //Button to complete the lesson, disabled until all modules are completed

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        //Retrieve the active language ID from the intent, finish activity if invalid
        Intent intent = getIntent();
        int activeLanguageID = intent.getIntExtra("activeLanguageID", -1);
        if(activeLanguageID < 0)
            finish();

        //Retrieve the active lesson ID from the intent, and load the corresponding lesson
        loadedLesson = new LessonList(activeLanguageID).getLesson(intent.getIntExtra("activeLessonID", -1));

        //Find the action bar and set it to display the loaded lesson name
        getSupportActionBar().setTitle(loadedLesson.getName());

        //Find the Complete button and disable it
        completeButton = findViewById(R.id.lesson_button_complete);
        completeButton.setEnabled(false);

        //Set the Complete button's onClick to complete the lesson and finish the activity
        ILessonList lList = new LessonList(loadedLesson.getLanguageID());
        completeButton.setOnClickListener(v ->
        {
            lList.completeLesson(loadedLesson);
            finish();
        });

        //If the lesson has already been completed, hide the Complete button entirely
        if(lList.lessonIsCompleted(loadedLesson))
            completeButton.setVisibility(View.GONE);

        //Create fragments from lesson modules
        for(ILessonModule m : loadedLesson.getModules())
            getSupportFragmentManager().beginTransaction().add(R.id.lesson_layout_modules,
                    LessonFragmentFactory.createLessonFragment(m, loadedLesson.getLanguageID())).commit();

        //Check whether to enable the Complete button from the start
        checkCompletedModules();
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
            Intent lookupIntent = DictionaryLookup.createLookupIntent(findViewById(R.id.lesson_layout_root),
                    new LanguageList().getLanguage(loadedLesson.getLanguageID()));
            if(lookupIntent != null)
                startActivity(lookupIntent);
            else //If no selected text found, notify the user
                Toast.makeText(LessonActivity.this, getResources().getString(R.string.no_text_selected), Toast.LENGTH_SHORT).show();

            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    //Check if all lesson modules are completed, and enable the Complete button if so
    public void checkCompletedModules()
    {
        boolean completed = true;
        for(ILessonModule m : loadedLesson.getModules())
            if(!m.isCompleted())
                completed = false;

        completeButton.setEnabled(completed);
    }
}