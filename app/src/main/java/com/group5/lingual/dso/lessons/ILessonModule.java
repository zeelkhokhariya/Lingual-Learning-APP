package com.group5.lingual.dso.lessons;

import java.io.Serializable;

//A unit of content within a lesson
public interface ILessonModule extends Serializable
{
    public boolean isCompleted();   //Whether the user has completed this module

    //Getters for the module's encoded content data,
    //encoded in a singular separator-delimited string for database storage
    //Throws an exception if the module's content has not been initialized
    public String getContentData();

    //Initialize the module's content using the given contentData from the database
    //Throws an exception if already initialized
    public void initializeContent(String contentData);
}
