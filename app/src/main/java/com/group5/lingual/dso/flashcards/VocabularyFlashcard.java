package com.group5.lingual.dso.flashcards;

import com.group5.lingual.data.DBManager;

//A variety of flashcard for testing users on the pronunciations and meanings of words
public class VocabularyFlashcard extends Flashcard
{
    //Question fields
    private String word;            //The word to test the user on

    //Answer fields
    private String pronunciation;   //The pronunciation of the word (in IPA, without enclosing [])
    private String definition;      //The definition of the word

    public VocabularyFlashcard(int id, int languageID,
                               String word,
                               String pronunciation, String definition)
    {
        super(id, languageID);

        this.word = word;

        this.pronunciation = pronunciation;
        this.definition = definition;

        contentInitialized = true;
    }

    //Create the flashcard without initializing its content
    public VocabularyFlashcard(int id, int languageID)
    {
        super(id, languageID);
    }

    @Override
    public String getQuestion()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return word;
    }

    @Override
    public String getAnswer()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return "[" + pronunciation + "]\n\n" + definition;
    }

    @Override
    public String getQuestionData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return word;
    }

    @Override
    public String getAnswerData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return pronunciation + DB_CONTENT_SEPARATOR + definition;
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
            word = qTokens[0];
            pronunciation = aTokens[0];
            definition = aTokens[1];
        }
        catch(IndexOutOfBoundsException e) //Throw an exception if any data not found
        {
            throw new IllegalArgumentException("Provided content data does not match card format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }
}