package com.group5.lingual.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group5.lingual.R;

//Displays a heading-and-content pair for use within an ItemDetailActivity
public class DetailSegmentFragment extends Fragment
{
    //Labels for the two arguments
    private static final String HEADING_ARG = "heading";
    private static final String CONTENT_ARG = "content";

    private String heading; //The heading of this segment
    private String content; //The content of this segment

    public DetailSegmentFragment() {}

    //Public getters for the argument labels
    public static String getHeadingArgumentLabel() { return HEADING_ARG; }
    public static String getContentArgumentLabel() { return CONTENT_ARG; }

    //Load the heading and content from the fragment's arguments
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            heading = getArguments().getString(HEADING_ARG);
            content = getArguments().getString(CONTENT_ARG);
        }
        else
            throw new IllegalStateException("Attempted to create item detail segment without arguments");
    }

    //Create the fragment's view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_detail_segment, container, false);
    }

    //Load the heading and content into the fragment's views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ((TextView)getView().findViewById(R.id.dseg_text_heading)).setText(heading);
        ((TextView)getView().findViewById(R.id.dseg_text_content)).setText(content);
    }
}