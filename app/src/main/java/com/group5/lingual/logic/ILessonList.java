package com.group5.lingual.logic;

import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.ILessonSummary;

import java.util.List;

//Provides access to all of the lessons for a given language
//All instances access and operate on the same persistent database
public interface ILessonList
{
    //Retrieve a lesson given its lesson ID number
    //Returns null if no such lesson is found
    public ILesson getLesson(int lessonID);

    //Retrieve a summary of a lesson given its lesson ID number
    //Returns null if no such lesson is found
    public ILessonSummary getLessonSummary(int lessonID);

    //Get a list of the lesson IDs of all the lessons for the language, in no guaranteed order
    public List<Integer> getAllIDs();

    //Counts the number of lessons for the language
    public int lessonCount();

    //Counts the number of unlocked lessons for the language
    public int unlockedLessonCount();

    //Sum of the estimated total time to complete all lessons for the language
    public int totalDuration();

    //Sum of the estimated time to complete all not-yet-completed lessons for the language
    public int remainingDuration();

    //Checks if a given lesson has been completed
    //Throws an exception if lesson does not belong to this list's language
    public boolean lessonIsCompleted(ILessonSummary l);

    //Sets the lesson to completed
    //Throws an exception if lesson does not belong to this list's language
    //Throws an exception if all modules are not complete
    //Throws an exception if the lesson is already complete
    public void completeLesson(ILesson l);

    //Checks if a given lesson has locked, based on whether its prerequisite has been completed
    //Throws an exception if lesson does not belong to this list's language
    public boolean lessonIsLocked(ILessonSummary l);
}
