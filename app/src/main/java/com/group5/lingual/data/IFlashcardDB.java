package com.group5.lingual.data;

import com.group5.lingual.dso.flashcards.IFlashcard;

import java.util.Collection;
import java.util.List;

//Holds and provides access to a collection of flashcards from all languages
//Flashcards are held in a sequence in ascending order of their due date
public interface IFlashcardDB
{
    //Get a flashcard in the database given its ID
    //Throws exception if no such flashcard is found
    public IFlashcard getFlashcard(int flashcardID);

    //Gets the first card in the sequence that matches the given language and tags
    //and whose due date is earlier than or equal to the given cutoff date (in milliseconds)
    //Does not remove the card from the sequence
    //Returns null if there are no such cards in the database
    public IFlashcard getFirstMatchingCard(int languageID, Collection<String> tags, long cutoffDate);

    //Gets the number of cards in the database that match the given language and tags
    //and whose due date is earlier than or equal to the given cutoff date (in milliseconds)
    public int cardCount(int languageID, Collection<String> tags, long cutoffDate);

    //Add a new card to the database, setting its due date and user's correct streak as given
    //The new card must not share its Flashcard ID with any other card in the database
    public void addCard(IFlashcard f, long dueDate, int streak);

    //Remove a card from the database
    //Returns the removed card if successful, throws exception otherwise
    public IFlashcard removeCard(IFlashcard f);

    //Gets the current streak of successive correct answers the user has given for a given card
    //Throws exception if no such flashcard is found
    public int getStreak(int flashcardID);

    //Gets a flashcard's due date
    //Throws exception if no such flashcard is found
    public long getDueDate(int flashcardID);

    //Reschedules a flashcard by setting a new due date for it
    //If the rescheduling is due to a correct answer on the card, increment the card's streak
    //If the rescheduling was for any other reason, reset the card's streak to zero
    //Throws exception if no such flashcard is found
    public void rescheduleCard(int flashcardID, long newDueDate, boolean answeredCorrectly);
}