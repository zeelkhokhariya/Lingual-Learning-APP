package com.group5.lingual.data.fakedb;

import com.group5.lingual.data.IFlashcardDB;
import com.group5.lingual.dso.flashcards.IFlashcard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

//Holds and provides access to a collection of flashcards from all languages
//Flashcards are held in a sequence, ordered in the order they were added to this database
//A fake implementation of a flashcard database using Lists
public class FakeFlashcardDB implements IFlashcardDB
{
    private List<IFlashcard> flashcards;        //The cards in the database
    private Map<Integer, Long> dueDates;        //Maps each flashcard ID to the card's due date (in milliseconds)
    private Map<Integer, Integer> streaks;      //Maps each flashcard ID to the user's correct streak on that card

    public FakeFlashcardDB()
    {
        flashcards = new ArrayList<IFlashcard>();
        dueDates = new HashMap<Integer, Long>();
        streaks = new HashMap<Integer, Integer>();
    }

    //Get a flashcard in the database given its ID
    //Throws exception if no such flashcard is found
    @Override
    public IFlashcard getFlashcard(int flashcardID)
    {
        for(IFlashcard f : flashcards)
            if(f.getID() == flashcardID)
                return f;

        throw new IllegalArgumentException("No flashcard with Flashcard ID " + flashcardID + " found in database");
    }

    //Gets the first card in the sequence that matches the given language and tags
    //and whose due date is earlier than or equal to the given cutoff date (in milliseconds)
    //Does not remove the card from the sequence
    //Returns null if there are no such cards in the database
    @Override
    public IFlashcard getFirstMatchingCard(int languageID, Collection<String> tags, long cutoffDate)
    {
        Iterator<IFlashcard> iterator = flashcards.iterator();
        while(iterator.hasNext())
        {
            IFlashcard f = iterator.next();
            if(f.getLanguageID() == languageID && f.hasTags(tags) && dueDates.get(f.getID()) <= cutoffDate)
                return f;
        }

        return null;
    }

    //Gets the number of cards in the database that match the given language and tags
    //and whose due date is earlier than or equal to the given cutoff date (in milliseconds)
    @Override
    public int cardCount(int languageID, Collection<String> tags, long cutoffDate)
    {
        int count = 0;

        for(IFlashcard f : flashcards)
            if(f.getLanguageID() == languageID && f.hasTags(tags) && dueDates.get(f.getID()) <= cutoffDate)
                count++;

        return count;
    }

    //Add a new card to the database, setting its due date and user's correct streak as given
    //The new card must not share its Flashcard ID with any other card in the database
    @Override
    public void addCard(IFlashcard f, long dueDate, int streak)
    {
        //Check for duplicate ID
        if(dueDates.containsKey(f.getID()))
            throw new IllegalArgumentException("Database already contains flashcard with ID " + f.getID());

        flashcards.add(f);
        dueDates.put(f.getID(), dueDate);
        streaks.put(f.getID(), streak);

        //Sort the flashcards in ascending order by due date
        Collections.sort(flashcards, (o1, o2) -> dueDates.get(o1.getID()).compareTo(dueDates.get(o2.getID())));
    }

    //Remove a card from the database
    //Returns the removed card if successful, throws exception otherwise
    @Override
    public IFlashcard removeCard(IFlashcard f)
    {
        if(flashcards.remove(f))
        {
            dueDates.remove(f.getID());
            streaks.remove(f.getID());

            return f;
        }
        else
            throw new IllegalArgumentException("Could not find flashcard to remove");
    }

    //Gets the current streak of successive correct answers the user has given for a given card
    //Throws exception if no such flashcard is found
    @Override
    public int getStreak(int flashcardID)
    {
        try
        {
            return streaks.get(flashcardID);
        }
        catch(NullPointerException e)
        {
            throw new IllegalArgumentException("No flashcard with Flashcard ID " + flashcardID + " found in database");
        }
    }

    //Gets a flashcard's due date
    //Throws exception if no such flashcard is found
    @Override
    public long getDueDate(int flashcardID)
    {
        try
        {
            return dueDates.get(flashcardID);
        }
        catch(NullPointerException e)
        {
            throw new IllegalArgumentException("No flashcard with Flashcard ID " + flashcardID + " found in database");
        }
    }

    //Reschedules a flashcard by setting a new due date for it
    //If the rescheduling is due to a correct answer on the card, increment the card's streak
    //Otherwise, if for any other reason (unlocking or wrong answer), reset the card's streak to zero
    //Throws exception if no such flashcard is found
    @Override
    public void rescheduleCard(int flashcardID, long newDueDate, boolean answeredCorrectly)
    {
        int newStreak = answeredCorrectly ? (getStreak(flashcardID) + 1) : 0;
        addCard(removeCard(getFlashcard(flashcardID)), newDueDate, newStreak);
    }
}