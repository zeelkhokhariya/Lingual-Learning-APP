package com.group5.lingual.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.group5.lingual.R;
import com.group5.lingual.logic.LanguageList;

import java.util.List;

//Displays a particular item, such as a word or character, as well as some given information about the item,
//organised into heading-content pairs
public class ItemDetailActivity extends DataSafeActivity
{
    //Labels for the activity's intent arguments
    private static final String ITEM_ARG = "item";
    private static final String HEADINGS_ARG = "segmentHeadings";
    private static final String CONTENTS_ARG = "segmentContents";
    private static final String ACTIVE_LANGUAGE_ARG = "activeLanguageID";

    private String item;                    //The item to display
    private List<String> segmentHeadings;   //Ordered list of headings to display below the item
    private List<String> segmentContents;   //List of informational strings tied to the corresponding headings
    private int activeLanguageID;           //ID of the currently active language

    //Public getters for the argument labels
    public static String getItemArgumentLabel() { return ITEM_ARG; }
    public static String getHeadingsArgumentLabel() { return HEADINGS_ARG; }
    public static String getContentsArgumentLabel() { return CONTENTS_ARG; }
    public static String getActiveLanguageArgumentLabel() { return ACTIVE_LANGUAGE_ARG; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        //Get the activities data from the intent arguments
        Intent intent = getIntent();
        item = intent.getStringExtra(ITEM_ARG);

        activeLanguageID = intent.getIntExtra(ACTIVE_LANGUAGE_ARG, -1);
        if(activeLanguageID < 1)
            throw new IllegalArgumentException("Did not provide valid active language ID");

        try
        {
            segmentHeadings = (List<String>) intent.getSerializableExtra(HEADINGS_ARG);
            segmentContents = (List<String>) intent.getSerializableExtra(CONTENTS_ARG);
        }
        catch(ClassCastException e)
        {
            throw new IllegalArgumentException("Provided headings or contents were not lists of strings");
        }
        if(segmentHeadings == null || segmentContents == null)
            throw new IllegalArgumentException("Could not find list of headings or contents");
        if(segmentHeadings.size() != segmentContents.size())
            throw new IllegalArgumentException("Provided list of headings and contents were not matched in size");

        //Display the detailed item
        ((TextView)findViewById(R.id.detail_text_item)).setText(item);

        //For each heading-content pair, create a DetailSegmentFragment to display it
        for(int i = 0; i < segmentHeadings.size(); i++)
        {
            Fragment f = new DetailSegmentFragment();
            Bundle args = new Bundle();
            args.putString(DetailSegmentFragment.getHeadingArgumentLabel(), segmentHeadings.get(i));
            args.putString(DetailSegmentFragment.getContentArgumentLabel(), segmentContents.get(i));
            f.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.detail_layout_segments, f).commit();
        }
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
            Intent lookupIntent = DictionaryLookup.createLookupIntent(findViewById(R.id.detail_layout_root),
                    new LanguageList().getLanguage(activeLanguageID));
            if(lookupIntent != null)
                startActivity(lookupIntent);
            else //If no selected text found, notify the user
                Toast.makeText(ItemDetailActivity.this, getResources().getString(R.string.no_text_selected), Toast.LENGTH_SHORT).show();

            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}