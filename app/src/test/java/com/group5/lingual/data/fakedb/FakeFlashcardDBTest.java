package com.group5.lingual.data.fakedb;

import com.group5.lingual.data.DBManager;
import com.group5.lingual.dso.flashcards.IFlashcard;
import com.group5.lingual.dso.flashcards.SimpleFlashcard;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FakeFlashcardDBTest
{
    //The database being tested
    FakeFlashcardDB db;

    //Testing tag sets
    Set<String> tagsEmpty;          //An empty set of tags
    Set<String> tagsPartial;        //A set of some of the possible tags
    Set<String> tagsFull;           //A set of all of the possible tags

    //Testing flashcards
    IFlashcard fEmptyTagsDue1st;    //The first due card, has no tags
    IFlashcard fPartialTagsDue2nd;  //The second due card, has some of the possible tags
    IFlashcard fFullTagsDue3rd;     //The third due card, has all of the possible tags
    IFlashcard fWith3StreakDue4th;  //The fourth due card, has a starting streak of 3
    IFlashcard fNotDue;             //A card that is not due
    IFlashcard fDifferentLanguage;  //A card that is due but assigned to a different language
    IFlashcard fDuplicateID;        //A card not in the DB with an ID that is already taken in the DB
    IFlashcard fNotInDB;            //A card with a unique ID that is not the database

    @Before
    public void setup()
    {
        db = new FakeFlashcardDB();

        new FakeFlashcardDB().addCard(new SimpleFlashcard(111, 1, "Q", "A"), 100, 0);

        //Initialize tag sets
        tagsEmpty = new HashSet<String>();
        tagsPartial = new HashSet<String>();
        tagsFull = new HashSet<String>();
        tagsPartial.add("tag1");
        tagsPartial.add("tag3");
        tagsFull.add("tag1");
        tagsFull.add("tag2");
        tagsFull.add("tag3");

        //Initialize flashcards
        fEmptyTagsDue1st = new SimpleFlashcard(101, 1, "Q", "A");
        fPartialTagsDue2nd = new SimpleFlashcard(102, 1, "Q", "A");
        fFullTagsDue3rd = new SimpleFlashcard(103, 1, "Q", "A");
        fWith3StreakDue4th = new SimpleFlashcard(104, 1, "Q", "A");
        fNotDue = new SimpleFlashcard(105, 1, "Q", "A");
        fDifferentLanguage = new SimpleFlashcard(106, 2, "Q", "A");
        fDuplicateID = new SimpleFlashcard(101, 1, "Q", "A");
        fNotInDB = new SimpleFlashcard(107, 1, "Q", "A");

        //Add tags to cards
        for(String s : tagsPartial)
            fPartialTagsDue2nd.addTag(s);
        for(String s : tagsFull)
            fFullTagsDue3rd.addTag(s);

        //Add flashcards to database
        db.addCard(fEmptyTagsDue1st, 1, 0);
        db.addCard(fPartialTagsDue2nd, 2, 0);
        db.addCard(fFullTagsDue3rd, 3, 0);
        db.addCard(fWith3StreakDue4th, 4, 3);
        db.addCard(fNotDue, DBManager.getDateMax(), 0);
        db.addCard(fDifferentLanguage, 5, 0);
    }

    @Test
    public void getFlashcard()
    {
        //Ensure a new database does not contain a flashcard
        assertThrows(IllegalArgumentException.class, () -> new FakeFlashcardDB().getFlashcard(fEmptyTagsDue1st.getID()));

        //Ensure a card in the database is retrieved appropriately
        assertEquals(fEmptyTagsDue1st, db.getFlashcard(fEmptyTagsDue1st.getID()));

        //Ensure a card not in the database is not retrieved
        assertThrows(IllegalArgumentException.class, () -> db.getFlashcard(fNotInDB.getID()));
    }

    @Test
    public void getFirstMatchingCard()
    {
        //Ensure a new database has no matching card
        assertNull(new FakeFlashcardDB().getFirstMatchingCard(1, Collections.emptySet(), DBManager.getDateMax()));

        //Ensure the first flashcard is correct when searching with each level of tags
        assertEquals(fFullTagsDue3rd, db.getFirstMatchingCard(1, tagsFull, 100));
        assertEquals(fPartialTagsDue2nd, db.getFirstMatchingCard(1, tagsPartial, 100));
        assertEquals(fEmptyTagsDue1st, db.getFirstMatchingCard(1, tagsEmpty, 100));

        //Getting the first card again after getting it before should change its position in the queue
        assertEquals(fEmptyTagsDue1st, db.getFirstMatchingCard(1, tagsEmpty, 100));

        //Remove the first three cards and ensure the fourth card is correct
        db.removeCard(fEmptyTagsDue1st);
        db.removeCard(fPartialTagsDue2nd);
        db.removeCard(fFullTagsDue3rd);
        assertEquals(fWith3StreakDue4th, db.getFirstMatchingCard(1, Collections.emptySet(), 100));

        //Removing the last due card for the language should ensure no cards are found
        db.removeCard(fWith3StreakDue4th);
        assertNull(db.getFirstMatchingCard(1, Collections.emptySet(), 100));

        //Rescheduling a card into the queue should allow it to be retrieved
        db.rescheduleCard(fNotDue.getID(), 6, false);
        assertEquals(fNotDue, db.getFirstMatchingCard(1, Collections.emptySet(), 100));

        //Adding a card back into the database ahead of the previous one should make that one first
        db.addCard(fEmptyTagsDue1st, 1, 0);
        assertEquals(fEmptyTagsDue1st, db.getFirstMatchingCard(1, Collections.emptySet(), 100));
    }

    @Test
    public void cardCount()
    {
        //A new database should have a count of zero
        assertEquals(0, new FakeFlashcardDB().cardCount(1, Collections.emptySet(), DBManager.getDateMax()));

        //Ensure the count is correct when only restricted on language
        assertEquals(5, db.cardCount(1, Collections.emptySet(), DBManager.getDateMax()));

        //Ensure the count is correct when searching by tag and with a due date cutoff
        assertEquals(4, db.cardCount(1, Collections.emptySet(), 100));
        assertEquals(2, db.cardCount(1, tagsPartial, DBManager.getDateMax()));

        //Ensure that removing a card reduces the count, and that adding it back in increases the count
        db.removeCard(fEmptyTagsDue1st);
        assertEquals(4, db.cardCount(1, Collections.emptySet(), DBManager.getDateMax()));
        db.addCard(fEmptyTagsDue1st, 1, 0);
        assertEquals(5, db.cardCount(1, Collections.emptySet(), DBManager.getDateMax()));

        //Ensure that rescheduling a card to outside the range reduces the count when searching on the range, but not when unrestricted
        db.rescheduleCard(fEmptyTagsDue1st.getID(), 101, false);
        assertEquals(3, db.cardCount(1, Collections.emptySet(), 100));
        assertEquals(5, db.cardCount(1, Collections.emptySet(), DBManager.getDateMax()));
    }

    //While the other tests implicitly also test add and remove card,
    //it remains to ensure that adding a card with a duplicate ID throws an exception,
    //and that remove card returns and throws exceptions properly
    @Test
    public void addRemoveCard()
    {
        //Adding a card with a duplicate ID should throw an exception
        assertThrows(IllegalArgumentException.class, () -> db.addCard(fDuplicateID, 100, 1));

        //The above should occur even if they're the exact same card
        assertThrows(IllegalArgumentException.class, () -> db.addCard(fEmptyTagsDue1st, 100, 1));

        //Removing a card successfully should return that card
        assertEquals(fEmptyTagsDue1st, db.removeCard(fEmptyTagsDue1st));

        //Attempting to remove a card that is not in the deck should throw an exception
        assertThrows(IllegalArgumentException.class, () -> db.removeCard(fNotInDB));

        //Removing a card that was already removed should throw an exception for the same reason
        assertThrows(IllegalArgumentException.class, () -> db.removeCard(fEmptyTagsDue1st));

        //Now that the card that shared the ID at the start was removed, we should be able to add that first card
        db.addCard(fDuplicateID, 100, 1);
    }

    @Test
    public void getStreak()
    {
        assertEquals(0, db.getStreak(fEmptyTagsDue1st.getID()));
        assertEquals(3, db.getStreak(fWith3StreakDue4th.getID()));

        //Should throw exception if none found
        assertThrows(IllegalArgumentException.class, () -> db.getStreak(fNotInDB.getID()));
    }

    @Test
    public void getDueDate()
    {
        assertEquals(1, db.getDueDate(fEmptyTagsDue1st.getID()));
        assertEquals(4, db.getDueDate(fWith3StreakDue4th.getID()));
        assertEquals(DBManager.getDateMax(), db.getDueDate(fNotDue.getID()));

        //Should throw exception if none found
        assertThrows(IllegalArgumentException.class, () -> db.getDueDate(fNotInDB.getID()));
    }

    //Mostly tested implicitly in other tests
    @Test
    public void rescheduleCard()
    {
        //Should throw exception if none found
        assertThrows(IllegalArgumentException.class, () -> db.rescheduleCard(fNotInDB.getID(), 100, true));

        //Should change due date and increment streak if passed true for answeredCorrectly
        db.rescheduleCard(fWith3StreakDue4th.getID(), 101, true);
        assertEquals(101, db.getDueDate(fWith3StreakDue4th.getID()));
        assertEquals(4, db.getStreak(fWith3StreakDue4th.getID()));

        //Should change due date and reset streak if passed true for answeredCorrectly
        db.rescheduleCard(fWith3StreakDue4th.getID(), DBManager.getDateMax(), false);
        assertEquals(DBManager.getDateMax(), db.getDueDate(fWith3StreakDue4th.getID()));
        assertEquals(0, db.getStreak(fWith3StreakDue4th.getID()));
    }
}