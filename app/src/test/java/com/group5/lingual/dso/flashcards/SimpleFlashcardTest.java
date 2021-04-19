package com.group5.lingual.dso.flashcards;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class SimpleFlashcardTest
{
    @Test
    public void getID()
    {
        IFlashcard f = new SimpleFlashcard(123, 8,"My question", "My answer");
        assertEquals(123, f.getID());
    }

    @Test
    public void getLanguageID()
    {
        IFlashcard f = new SimpleFlashcard(123, 8,"My question", "My answer");
        assertEquals(8, f.getLanguageID());
    }

    //Should throw IllegalArgumentExceptions when trying to create a flashcard
    //with a non-positive Flashcard ID or Language ID
    @Test
    public void invalidID()
    {
        assertThrows(IllegalArgumentException.class, () -> new SimpleFlashcard(0, 1, "Q", "A"));
        assertThrows(IllegalArgumentException.class, () -> new SimpleFlashcard(-1, 1, "Q", "A"));
        assertThrows(IllegalArgumentException.class, () -> new SimpleFlashcard(1, 0, "Q", "A"));
        assertThrows(IllegalArgumentException.class, () -> new SimpleFlashcard(1, -1, "Q", "A"));
    }

    //Tests the addTag method while necessarily also testing the hasTag method
    @Test
    public void addTag()
    {
        IFlashcard f = new SimpleFlashcard(123, 8, "My question", "My answer");
        assertFalse(f.hasTag("tag1"));

        f.addTag("tag1");
        assertTrue(f.hasTag("tag1"));
    }

    //Tests the removeTag method while necessarily also testing the addTag and hasTag methods
    @Test
    public void removeTag()
    {
        IFlashcard f = new SimpleFlashcard(123, 8, "My question", "My answer");
        f.addTag("tag1");
        assertTrue(f.hasTag("tag1"));

        f.removeTag("tag1");
        assertFalse(f.hasTag("tag1"));
    }

    //Tests the multi-tag version of hasTag
    @Test
    public void hasTags()
    {
        IFlashcard f = new SimpleFlashcard(123, 8, "My question", "My answer");
        f.addTag("tag1");
        f.addTag("tag3");
        f.addTag("tag5");

        //Check against an empty set of tags
        Collection<String> testTags = new ArrayList<String>();
        assertTrue(f.hasTags(testTags));

        //Check against a subset of the card's full tag set
        testTags.add("tag1");
        assertTrue(f.hasTags(testTags));

        //Check against the card's full tag set
        testTags.add("tag3");
        testTags.add("tag5");
        assertTrue(f.hasTags(testTags));

        //Check against a superset of the card's full tag set
        testTags.add("tag2");
        testTags.add("tag4");
        assertFalse(f.hasTags(testTags));

        //Check against a set containing some in common with the card's set, and some not
        testTags.remove("tag5");
        testTags.remove("tag4");
        assertFalse(f.hasTags(testTags));
    }

    @Test
    public void getQuestion()
    {
        IFlashcard f = new SimpleFlashcard(123, 8, "My question", "My answer");
        assertEquals("My question", f.getQuestion());
    }

    @Test
    public void getAnswer()
    {
        IFlashcard f = new SimpleFlashcard(123, 8, "My question", "My answer");
        assertEquals("My answer", f.getAnswer());
    }

    @Test
    public void getQuestionData()
    {
        IFlashcard f = new SimpleFlashcard(123, 8, "My question", "My answer");
        assertEquals("My question", f.getQuestionData());
    }

    @Test
    public void getAnswerData()
    {
        IFlashcard f = new SimpleFlashcard(123, 8, "My question", "My answer");
        assertEquals("My answer", f.getAnswerData());
    }

    @Test
    public void equals()
    {
        //Test f against several objects, one equal and one different in each possible way
        IFlashcard f = new SimpleFlashcard(1, 1, "Q", "A");
        assertEquals(f, new SimpleFlashcard(1, 1, "Q", "A"));
        assertNotEquals(f, new SimpleFlashcard(2, 1, "Q", "A"));
        assertNotEquals(f, new SimpleFlashcard(1, 2, "Q", "A"));
        assertNotEquals(f, new SimpleFlashcard(1, 1, "R", "A"));
        assertNotEquals(f, new SimpleFlashcard(1, 1, "Q", "B"));
        assertNotEquals(f, null);
        assertNotEquals(f, 1);

        //Test equality when tags are involved
        f.addTag("tag");
        IFlashcard other = new SimpleFlashcard(1, 1, "Q", "A");
        assertNotEquals(f, other);
        other.addTag("tag");
        assertEquals(f, other);
    }
}