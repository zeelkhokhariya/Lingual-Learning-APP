package com.group5.lingual.logic;

import com.group5.lingual.data.DBManager;
import com.group5.lingual.data.IFlashcardDB;
import com.group5.lingual.dso.flashcards.IFlashcard;
import com.group5.lingual.dso.flashcards.SimpleFlashcard;
import com.group5.lingual.data.DBLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class FlashcardQueueIntegrationTest
{
    IFlashcardDB db;

    IFlashcardQueue queue;

    Collection<String> tags1 = new ArrayList<String>();
    Collection<String> tags2 = new ArrayList<String>();

    @Before
    public void setUp()
    {
        DBManager.setDBPathName(DBLoader.loadDB());
        db = DBManager.getFlashcardDB();

        queue = new FlashcardQueue(1);

        //Add 4 cards to the db, locked
        //The first two are untagged and in this language
        //The third is untagged and in a different language
        //The fourth is tagged matching tags1, and in this language
        //Repeat this with another 4 cards, but add them unlocked and due
        //Repeat this with another 4 cards, but add them unlocked but not due for some time

        db.addCard(new SimpleFlashcard(101, 1, "Q-1-1", "A-1-1"), DBManager.getDateMax(), 0);
        db.addCard(new SimpleFlashcard(102, 1, "Q-1-2", "A-1-2"), DBManager.getDateMax(), 0);
        db.addCard(new SimpleFlashcard(103, 2, "Q-1-3", "A-1-3"), DBManager.getDateMax(), 0);
        IFlashcard f4a = new SimpleFlashcard(104, 1, "Q-1-4", "A-1-4");
        f4a.addTag("tag");
        db.addCard(f4a, DBManager.getDateMax(), 0);

        db.addCard(new SimpleFlashcard(201, 1, "Q-2-1", "A-2-1"), System.currentTimeMillis(), 0);
        db.addCard(new SimpleFlashcard(202, 1, "Q-2-2", "A-2-2"), System.currentTimeMillis(), 0);
        db.addCard(new SimpleFlashcard(203, 2, "Q-2-3", "A-2-3"), System.currentTimeMillis(), 0);
        IFlashcard f4b = new SimpleFlashcard(204, 1, "Q-2-4", "A-2-4");
        f4b.addTag("tag");
        db.addCard(f4b, System.currentTimeMillis(), 0);

        db.addCard(new SimpleFlashcard(301, 1, "Q-3-1", "A-3-1"), DBManager.getDateMax() - 1, 0);
        db.addCard(new SimpleFlashcard(302, 1, "Q-3-2", "A-3-2"), DBManager.getDateMax() - 1, 0);
        db.addCard(new SimpleFlashcard(303, 2, "Q-3-3", "A-3-3"), DBManager.getDateMax() - 1, 0);
        IFlashcard f4c = new SimpleFlashcard(304, 1, "Q-3-4", "A-3-4");
        f4c.addTag("tag");
        db.addCard(f4c, DBManager.getDateMax() - 1, 0);


        tags1.add("tag");

        tags2.add("tag");
        tags2.add("othertag");
    }

    @After
    public void tearDown()
    {
        DBManager.shutdownDB();
        DBLoader.cleanup();
    }

    //Tests the getNextCard method filtering only by language
    @Test
    public void getNextCardNoTags()
    {
        //Ensure the correct card is initially next
        assertEquals(201, queue.getNextCard().getID());

        //Ensure that getting the next card does not remove it from it's position as next
        assertEquals(201, queue.getNextCard().getID());

        //Ensure the correct card after the first two are answered, one wrong and one right
        queue.answerCard(queue.getNextCard(), false);
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(204, queue.getNextCard().getID());

        //Answer 204, ensure that the wrongly-answered 201 is back at the front
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(201, queue.getNextCard().getID());

        //Ensure that after answering 201 correctly, the queue is empty and getNextCard returns null
        queue.answerCard(queue.getNextCard(), true);
        assertNull(queue.getNextCard());
    }

    //Tests the getNextCard method filtering with tag sets
    @Test
    public void getNextCardWithTags()
    {
        //No has all the tags in tags2, so searching for tags2 should return null
        assertNull(queue.getNextCard(tags2));

        //204 matches tags1, so searching for next with tags1 should return 204
        assertEquals(204, queue.getNextCard(tags1).getID());

        //After answering 204 correctly, there should be no due card in the queue that matches tags1
        queue.answerCard(queue.getNextCard(tags1), true);
        assertNull(queue.getNextCard(tags1));
    }

    @Test
    public void answerCard()
    {
        //Answering 201 wrong should put it after all other due cards, leaving 202 next
        queue.answerCard(queue.getNextCard(), false);
        assertEquals(202, queue.getNextCard().getID());

        //Answering 202 and 204 right should bring us back to 201
        queue.answerCard(queue.getNextCard(), true);
        queue.answerCard(queue.getNextCard(), true);
        IFlashcard f201 = queue.getNextCard();
        assertEquals(201, f201.getID());

        //Answering 201 right this time should delay it into the future, exhausting the due cards
        queue.answerCard(queue.getNextCard(), true);
        assertNull(queue.getNextCard());

        //Ensure an exception is thrown when trying to answer a card that is not due
        assertThrows(IllegalArgumentException.class, () -> queue.answerCard(f201, true));
        //Ensure an exception is thrown when trying to answer a card that is not in the database
        IFlashcard nonexistent = new SimpleFlashcard(205, 1, "Q", "A");
        assertThrows(IllegalArgumentException.class, () -> queue.answerCard(nonexistent, true));
        //Ensure an exception is thrown when trying to answer a card from another language
        IFlashcard otherLanguage = new SimpleFlashcard(206, 2, "Q", "A");
        db.addCard(otherLanguage, System.currentTimeMillis(), 0);
        assertThrows(IllegalArgumentException.class, () -> queue.answerCard(otherLanguage, true));

        //Answering 201 correctly before should have brought its streak to 1
        assertEquals(1, db.getStreak(201));

        //Adding a flashcard with a non-zero streak, then answering it wrong should bring its streak to zero
        IFlashcard streakCard = new SimpleFlashcard(207, 1, "Q", "A");
        db.addCard(streakCard, System.currentTimeMillis(), 2);
        queue.answerCard(streakCard, false);
        assertEquals(0, db.getStreak(streakCard.getID()));
    }

    @Test
    public void unlockCard()
    {
        //Exhaust the due cards by answering the three cards correctly
        queue.answerCard(queue.getNextCard(), true);
        queue.answerCard(queue.getNextCard(), true);
        queue.answerCard(queue.getNextCard(), true);
        assertNull(queue.getNextCard());

        //Unlock the locked cards of this language in the following order
        queue.unlockCard(104);
        queue.unlockCard(101);
        queue.unlockCard(102);

        //Ensure the next three cards are, in order, 104, 101, 102
        assertEquals(104, queue.getNextCard().getID());
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(101, queue.getNextCard().getID());
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(102, queue.getNextCard().getID());
        queue.answerCard(queue.getNextCard(), true);

        //Ensure the due cards are completely exhausted
        assertNull(queue.getNextCard());

        //Ensure an exception is thrown when trying to unlock a card that is not locked
        assertThrows(IllegalArgumentException.class, () -> queue.unlockCard(201));
        //Ensure an exception is thrown when trying to unlock a card that is not in the database
        assertThrows(IllegalArgumentException.class, () -> queue.unlockCard(105));
        //Ensure an exception is thrown when trying to unlock a card from another language
        assertThrows(IllegalArgumentException.class, () -> queue.unlockCard(103));
    }

    @Test
    public void dueCardCount()
    {
        //Ensure the due count starts at 3 cards
        assertEquals(3, queue.dueCardCount());

        //Answer a card correctly, which should reduce the due count by one
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(2, queue.dueCardCount());

        //Answer a card incorrectly, which should leave the due count count the same
        queue.answerCard(queue.getNextCard(), false);
        assertEquals(2, queue.dueCardCount());

        //Answer the next two correctly, exhausting the due cards
        queue.answerCard(queue.getNextCard(), true);
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(0, queue.dueCardCount());

        //Reschedule all of the unlocked cards so that they are due, bringing the due count to 6 cards
        db.rescheduleCard(201, System.currentTimeMillis(), false);
        db.rescheduleCard(202, System.currentTimeMillis(), false);
        db.rescheduleCard(204, System.currentTimeMillis(), false);
        db.rescheduleCard(301, System.currentTimeMillis(), false);
        db.rescheduleCard(302, System.currentTimeMillis(), false);
        db.rescheduleCard(304, System.currentTimeMillis(), false);
        assertEquals(6, queue.dueCardCount());

        //Unlock a card, bringing the due count to 7 cards
        queue.unlockCard(101);
        assertEquals(7, queue.dueCardCount());
    }

    @Test
    public void unlockedCardCount()
    {
        //Ensure the unlocked count starts at 6 cards
        assertEquals(6, queue.unlockedCardCount());

        //Answer a card correctly, which should leave the unlocked count the same
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(6, queue.unlockedCardCount());

        //Answer a card incorrectly, which should leave the unlocked count the same
        queue.answerCard(queue.getNextCard(), false);
        assertEquals(6, queue.unlockedCardCount());

        //Answer the next two correctly, leaving the unlocked count the same
        queue.answerCard(queue.getNextCard(), true);
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(6, queue.unlockedCardCount());

        //Reshuffle the discards into the draw pile, leaving the unlocked count the same
        db.rescheduleCard(201, System.currentTimeMillis(), false);
        db.rescheduleCard(202, System.currentTimeMillis(), false);
        db.rescheduleCard(204, System.currentTimeMillis(), false);
        db.rescheduleCard(301, System.currentTimeMillis(), false);
        db.rescheduleCard(302, System.currentTimeMillis(), false);
        db.rescheduleCard(304, System.currentTimeMillis(), false);
        assertEquals(6, queue.unlockedCardCount());

        //Unlock a card, bringing the unlocked count to 7 cards
        queue.unlockCard(101);
        assertEquals(7, queue.unlockedCardCount());
    }

    @Test
    public void totalCardCount()
    {
        //Ensure the total count starts at 9 cards
        assertEquals(9, queue.totalCardCount());

        //Answer a card correctly, which should leave the total count the same
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(9, queue.totalCardCount());

        //Answer a card incorrectly, which should leave the total count the same
        queue.answerCard(queue.getNextCard(), false);
        assertEquals(9, queue.totalCardCount());

        //Answer the next two correctly, leaving the total count the same
        queue.answerCard(queue.getNextCard(), true);
        queue.answerCard(queue.getNextCard(), true);
        assertEquals(9, queue.totalCardCount());

        //Reshuffle the discards into the draw pile, leaving the total count the same
        db.rescheduleCard(201, System.currentTimeMillis(), false);
        db.rescheduleCard(202, System.currentTimeMillis(), false);
        db.rescheduleCard(204, System.currentTimeMillis(), false);
        db.rescheduleCard(301, System.currentTimeMillis(), false);
        db.rescheduleCard(302, System.currentTimeMillis(), false);
        db.rescheduleCard(304, System.currentTimeMillis(), false);
        assertEquals(9, queue.totalCardCount());

        //Unlock a card, leaving the total count the same
        queue.unlockCard(101);
        assertEquals(9, queue.totalCardCount());
    }

    //Verify the values of a flashcard retrieved from the queue
    @Test
    public void retrievedFlashcardValues()
    {
        //Ensure the correct card is initially next
        assertEquals(201, queue.getNextCard().getID());

        //Ensure that the card has the correct values
        assertEquals(queue.getNextCard(), new SimpleFlashcard(201, 1, "Q-2-1", "A-2-1"));
    }
}