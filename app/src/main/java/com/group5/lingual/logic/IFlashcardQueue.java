package com.group5.lingual.logic;

import com.group5.lingual.dso.flashcards.IFlashcard;

import java.util.Collection;

//Provides access to all of the flashcards for a given language
//All instances access and operate on the same persistent databases
public interface IFlashcardQueue
{
    //Gets the next card in the queue that belongs to the given language,
    //and if applicable, has the given tags
    //Does not remove the card from the queue
    //Returns null if there are no such cards in the queue
    public IFlashcard getNextCard();
    public IFlashcard getNextCard(Collection<String> tags);

    //Take the appropriate actions upon answering a card in the queue correctly or incorrectly
    public void answerCard(IFlashcard f, boolean correct);

    //Move the card with the given ID into the card queue, if it is currently locked
    public void unlockCard(int flashcardID);

    //Gets the number of cards due, unlocked, and in total for this language
    public int dueCardCount();
    public int unlockedCardCount();
    public int totalCardCount();
}