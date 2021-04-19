package com.group5.lingual.dso.lessons;

//An interface for a brief description of a lesson in the app, but not its detailed contents
//Useful for retrieving information about a lesson without needing its content modules
public interface ILessonSummary
{
    public int getID();         //Getter for the unique lesson ID number, must be positive
    public int getLanguageID(); //Getter for the ID number of the Language this lesson is associated with

    public String getName();        //Getter for the name of the lesson to display
    public int getDuration();       //Getter for the estimated time to complete the lesson in minutes
    public String getIconName();    //Name of the drawable icon to display

    //Getter for the lesson ID of the the lesson that must be completed to unlock this lesson,
    //-1 if the lesson is unlocked from the start
    public int getPrerequisiteID();
}
