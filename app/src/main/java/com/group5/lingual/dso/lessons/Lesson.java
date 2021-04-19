package com.group5.lingual.dso.lessons;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//Describes a lesson available in the app
public class Lesson implements ILesson
{
    private int id;             //Unique lesson ID number, must be positive
    private int languageID;     //The ID number of the Language this lesson is associated with
    private String name;        //Name of the lesson to display
    private int duration;       //Estimated time to complete the lesson in minutes
    private String iconName;    //Name of the drawable icon to display

    //Lesson ID of the lesson that must be completed to unlock this lesson (-1 if no prerequisite)
    private int prerequisiteID;

    //List of flashcard IDs to be unlocked when the lesson is completed
    List<Integer> tiedFlashcards;

    private List<ILessonModule> modules; //Modules containing the lesson's content

    public Lesson(int lessonID, int languageID, String name, int duration, String iconName, int prerequisiteID, List<Integer> tiedFlashcards)
    {
        //Validate lesson ID
        if(lessonID <= 0)
            throw new IllegalArgumentException("Lesson ID must be positive");

        //Valid language ID
        if(languageID <= 0)
            throw new IllegalArgumentException("Language ID must be positive");

        //Validate duration
        if(duration <= 0)
            throw new IllegalArgumentException("Lesson duration must be positive");

        this.id = lessonID;
        this.languageID = languageID;
        this.name = name;
        this.duration = duration;
        this.iconName = iconName;
        this.prerequisiteID = prerequisiteID;
        this.tiedFlashcards = tiedFlashcards;

        modules = new ArrayList<ILessonModule>();
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        //Ensure the other is a lesson
        if(!(obj instanceof Lesson))
            return false;

        Lesson other = (Lesson)obj;

        //Compare values
        return getID() == other.getID()
                && getLanguageID() == other.getLanguageID()
                && getName().equals(other.getName())
                && getDuration() == other.getDuration()
                && getIconName().equals(other.getIconName())
                && getPrerequisiteID() == other.getPrerequisiteID()
                && getModules().equals(other.getModules())
                && getTiedFlashcards().equals(other.getTiedFlashcards());
    }

    //Basic getters for the lesson's data
    @Override public int getID() { return id; }
    @Override public int getLanguageID() { return languageID; }
    @Override public String getName() { return name; }
    @Override public int getDuration() { return duration; }
    @Override public String getIconName() { return iconName; }
    @Override public int getPrerequisiteID() { return prerequisiteID; }
    @Override public List<ILessonModule> getModules() { return modules; }
    @Override public List<Integer> getTiedFlashcards() { return tiedFlashcards; }

    //Adds module to the lesson (will be displayed in the order they were added to the lesson)
    public void addModule(ILessonModule module) { modules.add(module); }
}
