package com.group5.lingual.dso.lessons;

//An implementation of ILessonModule that lacks a default constructor, for testing purposes
public class WrongLessonModuleFake implements ILessonModule
{
    private String contentData;

    public WrongLessonModuleFake(String contentData)
    {
        this.contentData = contentData;
    }

    @Override
    public boolean isCompleted()
    {
        return false;
    }

    @Override
    public String getContentData()
    {
        return contentData;
    }

    @Override
    public void initializeContent(String contentData)
    {
        this.contentData = contentData;
    }
}
