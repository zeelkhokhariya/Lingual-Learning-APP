package com.group5.lingual.presentation.lessonfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group5.lingual.R;
import com.group5.lingual.dso.lessons.ItemsListLessonModule;
import com.group5.lingual.presentation.ItemDetailActivity;
import com.group5.lingual.presentation.LessonActivity;

import java.io.Serializable;
import java.util.List;

//Lesson fragment that displays a list of items which can be expanded to see their details
public class ItemsListLessonFragment extends Fragment
{
    private static final int NUM_COLUMNS = 3;   //Number of columns to spread items across

    private int activeLanguageID;               //Language ID of the currently active language
    private ItemsListLessonModule module;       //The module to display

    public ItemsListLessonFragment() {}

    //Load the serialized module from the fragment's arguments
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            activeLanguageID = getArguments().getInt(LessonFragmentFactory.getActiveLanguageArgumentLabel());
            module = (ItemsListLessonModule) getArguments().getSerializable(LessonFragmentFactory.getModuleArgumentLabel());
        }
        else
            throw new IllegalArgumentException("Attempted to create lesson fragment without appropriate arguments");
    }

    //Create the fragment's view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_lesson_items_list, container, false);
    }

    //Load the module's content into the fragment's views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Set the module heading
        ((TextView)getView().findViewById(R.id.lmod_ilist_text_heading)).setText(module.getHeading());

        //Get the list of items to display
        List<String> items = module.getItems();

        //Create an adapter to create the views to insert into the list
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_row_items, R.id.items_list_row_text_item, items);

        //Get the parent of the list columns and ensure it has the correct number of columns
        LinearLayout itemsTable = getView().findViewById(R.id.lmod_ilist_layout_items);
        if(itemsTable.getChildCount() != NUM_COLUMNS)
            throw new IllegalStateException("Fragment layout contains the wrong number of columns");

        //Get that layout's children to find the columns of the list
        LinearLayout[] columns = new LinearLayout[NUM_COLUMNS];
        for(int i = 0; i < NUM_COLUMNS; i++)
            columns[i] = (LinearLayout)itemsTable.getChildAt(i);

        //Add the items to the columns
        for(int i = 0; i < items.size(); i++)
        {
            //Get the view from the adapter
            View listRow = listAdapter.getView(i, null, columns[i % NUM_COLUMNS]);

            //Get the data that will be passed to the detail activity in the item's onClick
            String rowItem = items.get(i);
            List<String> rowContents = module.getItemContents(i);

            //Set the item's onClick to open a detail activity giving more information about the item
            listRow.setOnClickListener(v ->
            {
                Intent intent = new Intent(getContext(), ItemDetailActivity.class);
                intent.putExtra(ItemDetailActivity.getItemArgumentLabel(), rowItem);
                intent.putExtra(ItemDetailActivity.getHeadingsArgumentLabel(), (Serializable)module.getItemHeadings());
                intent.putExtra(ItemDetailActivity.getContentsArgumentLabel(), (Serializable)rowContents);
                intent.putExtra(ItemDetailActivity.getActiveLanguageArgumentLabel(), activeLanguageID);
                startActivity(intent);
            });

            //Insert into the appropriate column
            columns[i % NUM_COLUMNS].addView(listRow);
        }
    }
}
