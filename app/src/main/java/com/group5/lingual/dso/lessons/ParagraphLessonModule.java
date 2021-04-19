package com.group5.lingual.dso.lessons;

import androidx.annotation.Nullable;

//A simple lesson module containing plain text content
public class ParagraphLessonModule implements ILessonModule
{
    //Separator to allow multiple strings to be stored in the contentData column of the database
    //(ex. contentData column contains 'stringA~~~stringB~~~stringC')
    private static final String DB_CONTENT_SEPARATOR = "~~~";

    private boolean contentInitialized = false; //Whether the module's content has yet been initialized

    private String heading; //Title of the module
    private String content; //Content of the module

    public ParagraphLessonModule() {}

    public ParagraphLessonModule(String heading, String content)
    {
        this.heading = heading;
        this.content = content;
        contentInitialized = true;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        //Ensure the other is a paragraph lesson module
        if(!(obj instanceof ParagraphLessonModule))
            return false;

        ParagraphLessonModule other = (ParagraphLessonModule)obj;

        //Compare values
        return getContentData().equals(other.getContentData());
    }

    //A simple text module has nothing for the user to complete, so they are always completed
    @Override
    public boolean isCompleted()
    {
        return true;
    }

    @Override
    public String getContentData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return heading + DB_CONTENT_SEPARATOR + content;
    }

    @Override
    public void initializeContent(String contentData)
    {
        //Ensure module not already initialized
        if(contentInitialized)
            throw new IllegalStateException("Lesson module already initialized");

        //Tokenize the data string
        String[] tokens = contentData.split(DB_CONTENT_SEPARATOR);

        //Set the content using the tokens
        try
        {
            heading = tokens[0];
            content = tokens[1];
        }
        catch(IndexOutOfBoundsException e) //Throw an exception if any data not found
        {
            throw new IllegalArgumentException("Provided content data does not match card format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }

    public String getHeading()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return heading;
    }

    public String getContent()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return content;
    }
}
