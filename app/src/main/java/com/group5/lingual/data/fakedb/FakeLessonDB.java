package com.group5.lingual.data.fakedb;

import com.group5.lingual.data.ILessonDB;
import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.ILessonSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//Holds and provides access to a collection of lesson headers
//A fake implementation of a lesson database using Lists
public class FakeLessonDB implements ILessonDB
{
    private List<ILesson> lessons;              //List of lessons in the database
    private Set<Integer> usedIDs;               //Record of the Lesson IDs of all the lessons in the database
    private Map<Integer, Boolean> completedIDs; //Map of which IDs correspond to lessons that have been completed

    public FakeLessonDB()
    {
        lessons = new ArrayList<ILesson>();
        usedIDs = new HashSet<Integer>();
        completedIDs = new HashMap<Integer, Boolean>();
    }

    //Retrieves a lesson given its ID, throws exception if not found
    @Override
    public ILesson getLesson(int lessonID)
    {
        for(ILesson l : lessons)
            if(l.getID() == lessonID)
                return l;

        throw new IllegalArgumentException("Could not find Lesson with Lesson ID " + lessonID);
    }

    //Retrieves a summary of a lesson given its ID, throws exception if none found
    //In the case of the Fake DB, the lesson summary is simply the lesson itself,
    //as there is no additional step to retrieve the content modules
    @Override
    public ILessonSummary getLessonSummary(int lessonID)
    {
        return getLesson(lessonID);
    }

    //Gets a list of the lesson IDs of all the lessons in the database for a given language, in no guaranteed order
    @Override
    public List<Integer> getAllIDs(int languageID)
    {
        List<Integer> idList = new ArrayList<Integer>();
        for(ILesson l : lessons)
            if(l.getLanguageID() == languageID)
                idList.add(l.getID());

        return idList;
    }

    //Counts the number of lessons in the database for a given language
    @Override
    public int lessonCount(int languageID)
    {
        int count = 0;
        for(ILesson l : lessons)
            if (l.getLanguageID() == languageID)
                count++;

        return count;
    }

    //Counts the number of unlocked lessons in the database for a given language
    @Override
    public int unlockedLessonCount(int languageID)
    {
        int count = 0;
        for(ILesson l : lessons)
            if(l.getLanguageID() == languageID && (l.getPrerequisiteID() < 0 || getLessonCompletion(l.getPrerequisiteID())))
                count++;

        return count;
    }

    //Adds a lesson to the database and marks whether it has been completed
    //The new lesson must not share its Lesson ID with any other lesson in the database
    @Override
    public void addLesson(ILesson l, boolean completed)
    {
        //Check for duplicate ID
        if(usedIDs.contains(l.getID()))
            throw new IllegalArgumentException("Database already contains lesson with ID " + l.getID());

        lessons.add(l);
        usedIDs.add(l.getID());
        completedIDs.put(l.getID(), completed);
    }

    //Remove a lesson from the database
    //Returns the removed lesson if successful, throws exception otherwise
    @Override
    public ILesson removeLesson(ILesson l)
    {
        if(lessons.remove(l))
        {
            usedIDs.remove(l.getID());
            completedIDs.remove(l.getID());
            return l;
        }
        else
            throw new IllegalArgumentException("Could not find language to remove");
    }

    //Getter and setter for a lesson's completion status
    @Override
    public boolean getLessonCompletion(int lessonID)
    {
        if(!completedIDs.containsKey(lessonID))
            throw new IllegalArgumentException("Could not find Lesson with Lesson ID " + lessonID);

        return completedIDs.get(lessonID);
    }
    @Override
    public void setLessonCompletion(int lessonID, boolean completed)
    {
        if(!completedIDs.containsKey(lessonID))
            throw new IllegalArgumentException("Could not find Lesson with Lesson ID " + lessonID);

        completedIDs.put(lessonID, completed);
    }
}
