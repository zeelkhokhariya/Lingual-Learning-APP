package com.group5.lingual.dso.flashcards;

//A variety of flashcard for testing the user on the pronunciation of alphabetical symbols
public class AlphabetFlashcard extends Flashcard
{
    //Question fields
    private String symbol;          //The alphabetical symbol to test the user on

    //Answer fields
    private String pronunciation;   //Basic pronunciation of the symbol (in IPA, without enclosing [])

    public AlphabetFlashcard(int id, int languageID, String symbol, String pronunciation)
    {
        super(id, languageID);

        this.symbol = symbol;

        this.pronunciation = pronunciation;

        contentInitialized = true;
    }

    //Create the flashcard without initializing its content
    public AlphabetFlashcard(int id, int languageID)
    {
        super(id, languageID);
    }

    @Override
    public String getQuestion()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return symbol;
    }

    @Override
    public String getAnswer()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return "[" + pronunciation + "]";
    }

    @Override
    public String getQuestionData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return symbol;
    }

    @Override
    public String getAnswerData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return pronunciation;
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
            symbol = qTokens[0];
            pronunciation = aTokens[0];
        }
        catch(IndexOutOfBoundsException e) //Throw an exception if any data not found
        {
            throw new IllegalArgumentException("Provided content data does not match card format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }
}