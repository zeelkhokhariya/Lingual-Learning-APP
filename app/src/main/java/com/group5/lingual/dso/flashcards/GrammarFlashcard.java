package com.group5.lingual.dso.flashcards;

//A variety of flashcard for testing the user on grammar-related questions
public class GrammarFlashcard extends Flashcard
{
    private String question;    //The grammar question
    private String answer;      //The corresponding answer

    public GrammarFlashcard(int id, int languageID, String question, String answer)
    {
        super(id, languageID);

        this.question = question;
        this.answer = answer;

        contentInitialized = true;
    }

    //Create the flashcard without initializing its content
    public GrammarFlashcard(int id, int languageID)
    {
        super(id, languageID);
    }

    //Simply provide the question as is
    @Override
    public String getQuestion()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return question;
    }

    //Simply provide the answer as is
    @Override
    public String getAnswer()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return answer;
    }

    @Override
    public String getQuestionData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return question;
    }

    @Override
    public String getAnswerData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Flashcard content not initialized");

        return answer;
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
            question = qTokens[0];
            answer = aTokens[0];
        }
        catch(IndexOutOfBoundsException e) //Throw an exception if any data not found
        {
            throw new IllegalArgumentException("Provided content data does not match card format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }
}
