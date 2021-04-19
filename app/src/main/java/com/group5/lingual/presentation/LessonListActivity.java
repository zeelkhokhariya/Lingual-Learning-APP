package com.group5.lingual.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.lingual.R;
import com.group5.lingual.dso.lessons.ILessonSummary;
import com.group5.lingual.logic.ILessonList;
import com.group5.lingual.logic.LessonList;

import java.util.Collections;
import java.util.List;

public class LessonListActivity extends DataSafeActivity
{
    private ILessonList lessons;        //List of all lessons in the app
    private List<Integer> lessonIDs;    //List of lesson IDs in order matching the displayed list

    private int activeLanguageID;       //ID of the currently chosen language

    private ListView listView;          //Displayed containing the lesson options
    private MyAdapter listAdapter;      //Adapter for the lesson list

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);

        //Retrieve the active language ID from the intent, finish activity if invalid
        Intent intent = getIntent();
        activeLanguageID = intent.getIntExtra("activeLanguageID", -1);
        if(activeLanguageID < 0)
            finish();

        //Set action bar to display a prompt
        setTitle(R.string.less_list_prompt);

        //Get the list of lessons in the app
        lessons = new LessonList(activeLanguageID);

        //Get lesson ID list and sort if alphabetically by name
        lessonIDs = lessons.getAllIDs();
        Collections.sort(lessonIDs, (id1, id2) ->
        {
            return lessons.getLessonSummary(id1).getName().compareTo(lessons.getLessonSummary(id2).getName());
        });

        //Find the list for lessons
        listView = findViewById(R.id.less_list_lesson_list);

        //Create an adapter
        listAdapter = new MyAdapter(LessonListActivity.this, lessonIDs);
        listView.setAdapter(listAdapter);

        //Set on click listener for items in the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pointing, long id)
            {
                if(pointing >= 0 && pointing < lessonIDs.size())
                {
                    Intent intent = new Intent(LessonListActivity.this, LessonActivity.class);
                    intent.putExtra("activeLanguageID", activeLanguageID);
                    intent.putExtra("activeLessonID", lessonIDs.get(pointing));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        listAdapter.notifyDataSetChanged(); //Update on resume in case lessons unlocked or completed
    }

    class MyAdapter extends ArrayAdapter<Integer>
    {
        private Context context;            //Context of the adapter
        private List<Integer> lessonIDs;    //List of lesson IDs in order matching the displayed list

        MyAdapter (Context c, List<Integer> lessonIDs)
        {
            super(c, R.layout.list_row_lesson, R.id.less_list_row_text_title, lessonIDs);

            this.context = c;
            this.lessonIDs = lessonIDs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View row = layoutInflater.inflate(R.layout.list_row_lesson, parent, false);
            ImageView lessonIcon = row.findViewById(R.id.less_list_row_image_icon);
            ImageView completeOverlay = row.findViewById(R.id.less_list_row_image_complete);
            ImageView lockOverlay = row.findViewById(R.id.less_list_row_image_lock);
            TextView myTitle = row.findViewById(R.id.less_list_row_text_title);
            TextView myDescription = row.findViewById(R.id.less_list_row_text_description);

            //Get the summary of the lesson being listed in order to display its information
            ILessonSummary lesson = lessons.getLessonSummary(lessonIDs.get(position));

            //Check whether the lesson is locked, disable clicking it if so
            boolean isLocked = lessons.lessonIsLocked(lesson);
            row.setClickable(isLocked);

            //Set the title of the row to display the lesson's name
            myTitle.setText(lesson.getName());

            //Set the icons
            lessonIcon.setImageResource(getResources().getIdentifier(lesson.getIconName(), "drawable", getPackageName()));
            completeOverlay.setImageResource(R.drawable.ic_lesson_overlay_complete);
            lockOverlay.setImageResource(R.drawable.ic_lesson_overlay_lock);

            //Show or hide the lesson's completed and locked icons based on its status
            completeOverlay.setVisibility(lessons.lessonIsCompleted(lesson) ? View.VISIBLE : View.INVISIBLE);
            lockOverlay.setVisibility(isLocked ? View.VISIBLE : View.INVISIBLE);

            //Set the description to display the lesson's estimated duration
            int minutes = lesson.getDuration();
            myDescription.setText("~" + getResources().getQuantityString(R.plurals.app_minutes, minutes, minutes) + " long");

            return row;
        }
    }
}
