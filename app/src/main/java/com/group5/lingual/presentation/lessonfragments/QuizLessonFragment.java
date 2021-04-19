package com.group5.lingual.presentation.lessonfragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group5.lingual.R;
import com.group5.lingual.dso.lessons.QuizLessonModule;
import com.group5.lingual.presentation.LessonActivity;

//Lesson fragment that displays the contents of a QuizLessonModule
public class QuizLessonFragment extends Fragment
{
    private QuizLessonModule module; //The module to display

    private TextView questionText;
    private TextView answerText;

    private Button revealButton;
    private Button nextButton;

    public QuizLessonFragment() {}

    //Load the serialized module from the fragment's arguments
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            module = (QuizLessonModule)getArguments().getSerializable(LessonFragmentFactory.getModuleArgumentLabel());
        else
            throw new IllegalArgumentException("Attempted to create lesson fragment without module");
    }

    //Create the fragment's view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_lesson_quiz, container, false);
    }

    //Load the module's content into the fragment's views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ((TextView)getView().findViewById(R.id.lmod_quiz_text_heading)).setText(module.getHeading());

        //Get layout views
        questionText = getView().findViewById(R.id.lmod_quiz_text_question);
        answerText = getView().findViewById(R.id.lmod_quiz_text_answer);
        revealButton = getView().findViewById(R.id.lmod_quiz_button_reveal);
        nextButton = getView().findViewById(R.id.lmod_quiz_button_next);

        //Set the button listeners
        revealButton.setOnClickListener(v -> showAnswer(true));
        nextButton.setOnClickListener(v -> loadNextQuestion());

        //Load the first question
        loadNextQuestion();
    }

    //Set whether to show the answer to the user
    private void showAnswer(boolean show)
    {
        revealButton.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    //Load the next question and hide the answer
    //If the questions are exhausted, instead display a message indicating that
    private void loadNextQuestion()
    {
        if(module.next())
        {
            showAnswer(false);

            questionText.setText(module.getQuestion());
            answerText.setText(module.getAnswer());
        }
        else
        {
            getView().findViewById((R.id.lmod_quiz_layout_answer)).setVisibility(View.GONE);
            questionText.setText(getResources().getString(R.string.lmod_quiz_finished));

            //Inform the parent activity to recheck if all modules are completed
            Activity parentActivity = getActivity();
            if(parentActivity instanceof LessonActivity)
                ((LessonActivity)parentActivity).checkCompletedModules();
        }
    }
}
