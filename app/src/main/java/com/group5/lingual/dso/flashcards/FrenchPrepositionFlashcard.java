package com.group5.lingual.dso.flashcards;

//A variety of flashcard that tests the user on prepositions following certain verbs, applicable to French
public class FrenchPrepositionFlashcard extends Flashcard
{
    private String verb;        //The question to display on the card
    private String preposition; //The answer to display on the card, after revealing

    public FrenchPrepositionFlashcard(int id, int languageID, String verb, String preposition)
    {
        super(id, languageID);

        this.verb = verb;
        this.preposition = preposition;

        contentInitialized = true;
    }

    //Create the flashcard without initializing its content
    public FrenchPrepositionFlashcard(int id, int languageID)
    {
        super(id, languageID);
    }

    //Simply provide the stored String question as the card's question
    @Override
    public String getQuestion()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return "What preposition is used after " + verb + " when it is followed by an infinitive?";
    }

    //Simply provide the stored String answer as the card's answer
    @Override
    public String getAnswer()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return preposition;
    }

    @Override
    public String getQuestionData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return verb;
    }

    @Override
    public String getAnswerData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return preposition;
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
            verb = qTokens[0];
            preposition = aTokens[0];
        }
        catch(IndexOutOfBoundsException e) //Throw an exception if any data not found
        {
            throw new IllegalArgumentException("Provided content data does not match card format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }
}
