package com.group5.lingual.dso.flashcards;

import com.group5.lingual.dso.ITaggable;

//Interface for a flashcard containing a question and answer for the user to test themselves with
//A flashcard can be tagged with strings for categorization and filtration purposes
public interface IFlashcard extends ITaggable
{
    public int getID();         //Getter for the unique flashcard ID number
    public int getLanguageID(); //Getter for the ID number of the Language this card is associated with

    //Getters for the question and answer to be displayed when reviewing the card
    //Throws an exception if the flashcard's content has not been initialized
    public String getQuestion();
    public String getAnswer();

    //Getters for the flashcard's encoded question and answer data,
    //each encoded in singular separator-delimited strings for database storage
    //Throws an exception if the flashcard's content has not been initialized
    public String getQuestionData();
    public String getAnswerData();

    //Initialize the flashcard's content using the given questionData and answerData from the database
    //Throws an exception if already initialized
    public void initializeContent(String questionData, String answerData);
}