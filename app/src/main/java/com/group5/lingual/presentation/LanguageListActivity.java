package com.group5.lingual.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.group5.lingual.R;
import com.group5.lingual.dso.languages.ILanguage;
import com.group5.lingual.logic.AppInitializer;
import com.group5.lingual.logic.FlashcardQueue;
import com.group5.lingual.logic.ILanguageList;
import com.group5.lingual.logic.ILessonList;
import com.group5.lingual.logic.LanguageList;
import com.group5.lingual.logic.LessonList;
import com.group5.lingual.logic.SaveManager;

import java.util.Collections;
import java.util.List;

public class LanguageListActivity extends DataSafeActivity
{
    private ILanguageList languages;     //List of all languages in the app
    private ListView listView;          //Displayed containing the language options
    private List<Integer> languageIDs;  //List of language IDs in order matching the displayed list

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_list);

        //As the 'main' activity, perform app initialization
        AppInitializer.initialize(this);

        //Set action bar to display a prompt
        setTitle(R.string.lang_prompt);

        //Get the list of languages in the app
        languages = new LanguageList();

        //Get language ID list and sort if alphabetically by name
        languageIDs = languages.getAllIDs();
        Collections.sort(languageIDs, (id1, id2) ->
        {
            return languages.getLanguage(id1).getName().compareTo(languages.getLanguage(id2).getName());
        });

        //Find the list
        listView = findViewById(R.id.lang_list_language_list);

        //Create an adapter
        MyAdapter adapter = new MyAdapter(LanguageListActivity.this, languageIDs);
        listView.setAdapter(adapter);

        //Set on click listener for items in the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pointing, long id)
            {
                if(pointing >= 0 && pointing < languageIDs.size())
                {
                    Intent intent = new Intent(LanguageListActivity.this, LearnOrReviewActivity.class);
                    intent.putExtra("activeLanguageID", languageIDs.get(pointing));
                    startActivity(intent);
                }
            }
        });
    }

    //Display a menu item to reset the database
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_database_reset, menu);
        return true;
    }

    //Handle when the user clicks the menu item to reset the database
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menu_item_database_reset)
        {
            //Create and show a confirmation dialog ensuring the user wants to reset the database
            AlertDialog.Builder confirmation = new AlertDialog.Builder(this);
            confirmation.setTitle(R.string.lang_reset_database_title);
            confirmation.setMessage(R.string.lang_reset_database_message);
            //If the user answers yes,
            confirmation.setPositiveButton(android.R.string.yes, (dialog, which) ->
            {
                //Save the existing data to ensure nothing is pending
                SaveManager.save();

                //Reinitialize the app and reset it to its original default state
                AppInitializer.initialize(this, true);

                //Restart the activity to reflect the new database
                finish();
                startActivity(getIntent());
            });
            //Otherwise do nothing
            confirmation.setNegativeButton(android.R.string.no, null);
            confirmation.show();

            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends ArrayAdapter<Integer>
    {
        private Context context;            //Context of the adapter
        private List<Integer> languageIDs;  //List of language IDs in order matching the displayed list

        MyAdapter (Context c, List<Integer> languageIDs)
        {
            super(c, R.layout.list_row_language, R.id.lang_row_text_title, languageIDs);//, title);

            this.context = c;
            this.languageIDs = languageIDs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View row = layoutInflater.inflate(R.layout.list_row_language, parent, false);
            ImageView images = row.findViewById(R.id.lang_row_image_icon);
            TextView myTitle = row.findViewById(R.id.lang_row_text_title);
            TextView myDescription = row.findViewById(R.id.lang_row_text_description);

            //Get the language being listed in order to display its information
            ILanguage language = languages.getLanguage(languageIDs.get(position));

            //Set the title of the row to display the language's name
            myTitle.setText(language.getName());

            //Fetch the icon ID corresponding to the language's icon name and use it as the row icon
            images.setImageResource(getResources().getIdentifier(language.getIconName(), "drawable", getPackageName()));

            //Set the description to display the language course's total lessons and cards
            int cards = new FlashcardQueue(language.getID()).totalCardCount();
            ILessonList lList = new LessonList(language.getID());
            int lessons = lList.lessonCount();
            String description = "Course contains "
                    + getResources().getQuantityString(R.plurals.app_cards, cards, cards)
                    + " and " + getResources().getQuantityString(R.plurals.app_lessons, lessons, lessons);

            if(lessons > 0) //If lessons available, show a time estimate as well
            {
                int minutes = lList.totalDuration();
                description += " (~" + getResources().getQuantityString(R.plurals.app_minutes, minutes, minutes) + " long)";
            }

            myDescription.setText(description);

            return row;
        }
    }
}
