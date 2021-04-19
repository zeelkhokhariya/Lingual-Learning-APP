package com.group5.lingual.logic;

import com.group5.lingual.data.DBManager;
import com.group5.lingual.data.IFlashcardDB;
import com.group5.lingual.data.ILessonDB;
import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.ILessonModule;
import com.group5.lingual.dso.lessons.ILessonSummary;

import java.util.List;

//Provides access to all of the lessons for a given language
//All instances access and operate on the same persistent database by default,
//unless supplied with a different one in the constructor
public class LessonList implements ILessonList
{
    private int languageID;                 //The language of the lessons this list contains
    private ILessonDB lessonDB;             //Database of all lessons in the app
    private IFlashcardQueue flashcardQueue; //Flashcard queue for this list's language

    public LessonList(int languageID)
    {
        this.languageID = languageID;
        lessonDB = DBManager.getLessonDB();
        flashcardQueue = new FlashcardQueue(languageID);
    }

    public LessonList(int languageID, ILessonDB lessonDB, IFlashcardQueue flashcardQueue)
    {
        this.languageID = languageID;
        this.lessonDB = lessonDB;
        this.flashcardQueue = flashcardQueue;
    }

    //Retrieve a lesson given its lesson ID number
    //Returns null if no such lesson is found
    @Override
    public ILesson getLesson(int lessonID)
    {
        //Get the lesson
        ILesson l = lessonDB.getLesson(lessonID);

        //Ensure the lesson belongs to this language
        if(l.getLanguageID() != languageID)
            throw new IllegalArgumentException("Lesson does not belong to this list's language");

        return l;
    }

    //Retrieve a summary of a lesson given its lesson ID number
    //Returns null if no such lesson is found
    @Override
    public ILessonSummary getLessonSummary(int lessonID)
    {
        //Get the lesson summary
        ILessonSummary l = lessonDB.getLessonSummary(lessonID);

        //Ensure the lesson belongs to this language
        if(l.getLanguageID() != languageID)
            throw new IllegalArgumentException("Lesson does not belong to this list's language");

        return l;
    }

    //Get a list of the lesson IDs of all the lessons for the language, in no guaranteed order
    @Override
    public List<Integer> getAllIDs()
    {
        return lessonDB.getAllIDs(languageID);
    }

    //Counts the number of lessons for the language
    @Override
    public int lessonCount()
    {
        return lessonDB.lessonCount(languageID);
    }

    //Counts the number of unlocked lessons for the language
    @Override
    public int unlockedLessonCount() { return lessonDB.unlockedLessonCount(languageID); }

    //Sum of the estimated total time to complete all lessons for the language
    @Override
    public int totalDuration()
    {
        int sum = 0;

        List<Integer> lessonIDs = getAllIDs();
        for(Integer id : lessonIDs)
            sum += getLessonSummary(id).getDuration();

        return sum;
    }

    //Sum of the estimated time to complete all not-yet-completed lessons for the language
    @Override
    public int remainingDuration()
    {
        int sum = 0;

        List<Integer> languageIDs = getAllIDs();
        for(Integer id : languageIDs)
        {
            ILessonSummary l = getLessonSummary(id);
            if(!lessonIsCompleted(l))
                sum += l.getDuration();
        }

        return sum;
    }

    //Checks if a given lesson has been completed
    //Throws an exception if lesson does not belong to this list's language
    @Override
    public boolean lessonIsCompleted(ILessonSummary l)
    {
        //Ensure the lesson belongs to this language
        if(l.getLanguageID() != languageID)
            throw new IllegalArgumentException("Lesson does not belong to this list's language");

        return lessonDB.getLessonCompletion(l.getID());
    }

    //Sets the lesson to completed
    //Throws an exception if lesson does not belong to this list's language
    //Throws an exception if all modules are not complete
    //Throws an exception if the lesson is already complete
    @Override
    public void completeLesson(ILesson l)
    {
        //Ensure the lesson belongs to this language
        if(l.getLanguageID() != languageID)
            throw new IllegalArgumentException("Lesson does not belong to this list's language");

        //Ensure the lesson is not locked
        if(lessonIsLocked(l))
            throw new IllegalArgumentException("Lesson cannot be completed until it is unlocked");

        //Ensure the lesson has not yet been completed
        if(lessonIsCompleted(l))
            throw new IllegalStateException("Lesson already completed");

        //Ensure all modules in the lesson have been completed already
        for(ILessonModule m : l.getModules())
            if(!m.isCompleted())
                throw new IllegalStateException("All modules must be completed before the lesson can be completed");

        //Mark the lesson as completed
        lessonDB.setLessonCompletion(l.getID(), true);

        //Unlock the flashcards tied to the lesson
        for(int fID : l.getTiedFlashcards())
            flashcardQueue.unlockCard(fID);
    }

    //Checks if a given lesson has locked, based on whether its prerequisite has been completed
    //Throws an exception if lesson does not belong to this list's language
    @Override
    public boolean lessonIsLocked(ILessonSummary l)
    {
        //Ensure the lesson belongs to this language
        if(l.getLanguageID() != languageID)
            throw new IllegalArgumentException("Lesson does not belong to this list's language");

        int prerequisiteID = l.getPrerequisiteID();

        return prerequisiteID >= 0 && !lessonIsCompleted(lessonDB.getLesson(prerequisiteID));
    }
}