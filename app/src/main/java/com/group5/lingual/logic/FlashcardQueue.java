package com.group5.lingual.logic;

import com.group5.lingual.data.DBManager;
import com.group5.lingual.data.IFlashcardDB;
import com.group5.lingual.dso.flashcards.IFlashcard;

import java.util.Collection;
import java.util.Collections;

//Provides access to all of the flashcards for a given language
//All instances access and operate on the same persistent databases by default,
//unless supplied with a different three in the constructor
public class FlashcardQueue implements IFlashcardQueue
{
    //When a question is answered correctly, reschedule a card to
    //(floor((user's streak)^INTERVAL_EXPONENT))*INTERVAL_UNIT milliseconds in the future
    private static final long INTERVAL_UNIT = 1000*60*60*24; //1 day
    private static final double INTERVAL_EXPONENT = 1.8;

    private IFlashcardDB flashcardDB;   //Database of all flashcards in the app
    private int languageID;             //The language of the flashcards this queue contains

    public FlashcardQueue(int languageID)
    {
        flashcardDB = DBManager.getFlashcardDB(); //Fetch the database singleton
        this.languageID = languageID;
    }

    public FlashcardQueue(int languageID, IFlashcardDB flashcardDB)
    {
        this.flashcardDB = flashcardDB;
        this.languageID = languageID;
    }

    //Get the next available card from this language, without removing it from the pile
    @Override
    public IFlashcard getNextCard()
    {
        return getNextCard(Collections.emptySet());
    }

    //Get the next available card from this language with the given tags,
    //without removing it from the pile
    @Override
    public IFlashcard getNextCard(Collection<String> tags)
    {
        return flashcardDB.getFirstMatchingCard(languageID, tags, System.currentTimeMillis());
    }

    //Answer a given card, correctly or incorrectly
    //If a card is answered correctly, move it from the available pile to the discard pile
    //If a card is answered incorrectly, move it to the bottom of the draw pile
    @Override
    public void answerCard(IFlashcard f, boolean correct)
    {
        //Ensure card belongs to this language
        if(f.getLanguageID() != languageID)
            throw new IllegalArgumentException("Flashcard does not belong to this queue's language");

        //Ensure card is unlocked
        if(flashcardDB.getDueDate(f.getID()) > System.currentTimeMillis())
            throw new IllegalArgumentException("Only cards that are due can be answered");

        if(correct) //If right, schedule for future based on the user's correct answer streak on this card
        {
            flashcardDB.rescheduleCard(f.getID(), System.currentTimeMillis()
                    + getCardInterval(flashcardDB.getStreak(f.getID()) + 1), true);
        }
        else //If wrong, schedule for immediate review
            flashcardDB.rescheduleCard(f.getID(), System.currentTimeMillis(), false);
    }

    //Schedules a locked card to be immediately available in the queue
    @Override
    public void unlockCard(int flashcardID)
    {
        if(flashcardDB.getFlashcard(flashcardID).getLanguageID() != languageID)
            throw new IllegalArgumentException("Flashcard does not belong to this queue's language");

        if(flashcardDB.getDueDate(flashcardID) != DBManager.getDateMax())
            throw new IllegalArgumentException("Flashcard is not locked to begin with");

        flashcardDB.rescheduleCard(flashcardID, System.currentTimeMillis(), false);
    }

    //Gets the number of cards in queue for this language
    @Override
    public int dueCardCount()
    {
        return flashcardDB.cardCount(languageID, Collections.emptySet(), System.currentTimeMillis());
    }

    //Gets the number of cards unlocked for this language
    @Override
    public int unlockedCardCount()
    {
        return flashcardDB.cardCount(languageID, Collections.emptySet(), DBManager.getDateMax() - 1);
    }

    //Gets the number of cards in total for this language
    @Override
    public int totalCardCount()
    {
        return flashcardDB.cardCount(languageID, Collections.emptySet(), DBManager.getDateMax());
    }

    //Determines how far in the future (in milliseconds) to schedule a card after a correct answer,
    //based on the user's current correct answer streak on that card
    private long getCardInterval(int streak)
    {
        return ((int)Math.pow(streak, INTERVAL_EXPONENT)) * INTERVAL_UNIT;
    }
}