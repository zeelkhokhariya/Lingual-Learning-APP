package com.group5.lingual.dso.flashcards;

import com.group5.lingual.data.DBManager;

//A variety of flashcard for testing the user on the meaning and reading of logograms
public class LogogramFlashcard extends Flashcard
{
    //Question fields
    private String character;   //The character to test the user on

    //Answer fields
    private String reading;     //The reading(s) of the character, transliterated into Latin script
    private String meaning;     //The general meaning of the character

    public LogogramFlashcard(int id, int languageID,
                             String character,
                             String reading, String meaning)
    {
        super(id, languageID);

        this.character = character;

        this.reading = reading;
        this.meaning = meaning;

        contentInitialized = true;
    }

    //Create the flashcard without initializing its content
    public LogogramFlashcard(int id, int languageID)
    {
        super(id, languageID);
    }

    @Override
    public String getQuestion()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return character;
    }

    @Override
    public String getAnswer()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return reading + "\n\n" + meaning;
    }

    @Override
    public String getQuestionData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return character;
    }

    @Override
    public String getAnswerData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return reading + DB_CONTENT_SEPARATOR + meaning;
    }

    @Override
    public void initializeContent(String questionData, String answerData)
    {
        //Ensure card not already initialized
        if(contentInitialized)
            throw new IllegalStateException("Flashcard already initialized");

        //Tokenize the data strings
        String[] qTokens = questionData.split(DB_CONTENT_SEPARATOR);
        String[] aTokens = answerData.split(DB_CONTENT_SEPARATOR);

        //Set the content using the tokens
        try
        {
            character = qTokens[0];
            reading = aTokens[0];
            meaning = aTokens[1];
        }
        catch(IndexOutOfBoundsException e) //Throw an exception if any data not found
        {
            throw new IllegalArgumentException("Provided content data does not match card format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }
}