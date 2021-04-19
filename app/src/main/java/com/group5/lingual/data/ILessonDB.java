package com.group5.lingual.data;

import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.ILessonSummary;

import java.util.List;

//Holds and provides access to a collection of lesson headers
public interface ILessonDB
{
    //Retrieve a lesson given its lesson ID number
    //Throws an exception if no such lesson is found
    public ILesson getLesson(int lessonID);

    //Retrieve a summary of a lesson given its lesson ID number
    //Throws an exception if no such lesson is found
    public ILessonSummary getLessonSummary(int lessonID);

    //Get a list of the lesson IDs of all the lessons in the database for a given language, in no guaranteed order
    public List<Integer> getAllIDs(int languageID);

    //Counts the number of lessons in the database for a given language
    public int lessonCount(int languageID);

    //Counts the number of unlocked lessons in the database for a given language
    public int unlockedLessonCount(int languageID);

    //Adds a lesson type to the database and marks whether it has been completed
    //The new lesson must not share its lesson ID with any other lesson in the database
    public void addLesson(ILesson l, boolean completed);

    //Remove a lesson from the database
    //Returns the removed lesson if successful, throws exception otherwise
    public ILesson removeLesson(ILesson l);

    //Getter and setter for whether a lesson should be marked as completed
    public boolean getLessonCompletion(int lessonID);
    public void setLessonCompletion(int lessonID, boolean completed);
}