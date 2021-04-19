package com.group5.lingual.dso.lessons;

//An implementation of ILessonModule that is properly defined, for testing purposes
public class ProperLessonModuleFake implements ILessonModule
{
    private String contentData;

    public ProperLessonModuleFake() {}

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
