package com.group5.lingual.dso.flashcards;

public class WrongFlashcardFake extends Flashcard
{
    private String question;
    private String answer;

    public WrongFlashcardFake()
    {
        super(1, 1);
    }

    @Override
    public String getQuestion()
    {
        return question;
    }

    @Override
    public String getAnswer()
    {
        return answer;
    }

    @Override
    public String getQuestionData()
    {
        return question;
    }

    @Override
    public String getAnswerData()
    {
        return answer;
    }

    @Override
    public void initializeContent(String questionData, String answerData)
    {
        question = questionData;
        answerData = answerData;
    }
}
