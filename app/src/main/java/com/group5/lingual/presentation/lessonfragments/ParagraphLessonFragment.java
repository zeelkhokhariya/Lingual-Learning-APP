package com.group5.lingual.presentation.lessonfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group5.lingual.R;
import com.group5.lingual.dso.lessons.ParagraphLessonModule;

//Lesson fragment that displays the contents of a ParagraphLessonModule
public class ParagraphLessonFragment extends Fragment
{
    private ParagraphLessonModule module; //The module to display

    public ParagraphLessonFragment() {}

    //Load the serialized module from the fragment's arguments
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            module = (ParagraphLessonModule)getArguments().getSerializable(LessonFragmentFactory.getModuleArgumentLabel());
        else
            throw new IllegalArgumentException("Attempted to create lesson fragment without module");
    }

    //Create the fragment's view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_lesson_paragraph, container, false);
    }

    //Load the module's content into the fragment's views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ((TextView)getView().findViewById(R.id.lmod_para_text_heading)).setText(module.getHeading());
        ((TextView)getView().findViewById(R.id.lmod_para_text_content)).setText(module.getContent());
    }
}