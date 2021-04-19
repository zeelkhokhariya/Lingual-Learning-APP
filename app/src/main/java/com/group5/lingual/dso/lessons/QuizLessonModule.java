package com.group5.lingual.dso.lessons;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//A lesson module that tests the user with a series of questions
public class QuizLessonModule implements ILessonModule
{
    //Separator to allow multiple strings to be stored in the contentData column of the database
    //(ex. contentData column contains 'stringA~~~stringB~~~stringC')
    private static final String DB_CONTENT_SEPARATOR = "~~~";

    private boolean contentInitialized = false; //Whether the module's content has yet been initialized

    private String heading;         //Title displayed above the quiz
    private List<String> questions; //Ordered list of quiz questions
    private List<String> answers;   //List of quiz answers in same order as the questions
    private int current = -1;       //The index of the currently selected question

    public QuizLessonModule() {}

    public QuizLessonModule(String heading, List<String> questions, List<String> answers)
    {
        if(questions.size() != answers.size())
            throw new IllegalArgumentException("Number of questions did not match number of answers");

        this.heading = heading;
        this.questions = questions;
        this.answers = answers;

        contentInitialized = true;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        //Ensure the other is a quiz lesson module
        if(!(obj instanceof QuizLessonModule))
            return false;

        QuizLessonModule other = (QuizLessonModule)obj;

        //Compare values
        return getContentData().equals(other.getContentData());
    }

    //Determines whether the quiz is completed based on whether any questions remain
    @Override
    public boolean isCompleted()
    {
        return exhaustedQuestions();
    }

    @Override
    public String getContentData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        //Add to the content data string the heading, followed by each question-answer pair (one token for each question, one for each answer)
        String result = heading + DB_CONTENT_SEPARATOR;
        for(int i = 0; i < questions.size(); i++)
            result += questions.get(i) + DB_CONTENT_SEPARATOR + answers.get(i) + DB_CONTENT_SEPARATOR;

        //Remove final trailing separator
        result = result.substring(0, result.length() - DB_CONTENT_SEPARATOR.length());

        return result;
    }

    @Override
    public void initializeContent(String contentData)
    {
        //Ensure module not already initialized
        if(contentInitialized)
            throw new IllegalStateException("Lesson module already initialized");

        //Tokenize the data string
        String[] tokens = contentData.split(DB_CONTENT_SEPARATOR);

        if(tokens.length % 2 != 1)
            throw new IllegalArgumentException("Provided content data does not match card format, tokens not odd");

        //Initialize the question and answer lists
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();

        //Set the content using the tokens
        try
        {
            heading = tokens[0];
            for(int i = 0; i < (tokens.length - 1); i += 2)
            {
                questions.add(tokens[i+1]);
                answers.add(tokens[i+2]);
            }
        }
        catch(IndexOutOfBoundsException e) //Throw an exception if any data not found
        {
            throw new IllegalArgumentException("Provided content data does not match module format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }

    public String getHeading()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return heading;
    }

    public String getQuestion()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        if(current < 0)
            throw new IllegalStateException("No question selected");

        if(exhaustedQuestions())
            throw new IllegalStateException("Quiz already exhausted");

        return questions.get(current);
    }

    public String getAnswer()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        if(current < 0)
            throw new IllegalStateException("No question selected");

        if(exhaustedQuestions())
            throw new IllegalStateException("Quiz already exhausted");

        return answers.get(current);
    }

    //Proceed to the next question in the quiz
    //Returns true if another question was found, false otherwise
    public boolean next()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        if(!exhaustedQuestions())
            current++;

        return !exhaustedQuestions();
    }

    //Determine if all of the questions in the quiz have been exhausted
    private boolean exhaustedQuestions()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return current >= questions.size();
    }
}
