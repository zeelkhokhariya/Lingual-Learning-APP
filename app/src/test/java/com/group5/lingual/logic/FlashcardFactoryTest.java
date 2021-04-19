package com.group5.lingual.logic;

import com.group5.lingual.dso.flashcards.IFlashcard;
import com.group5.lingual.dso.flashcards.ProperFlashcardFake;
import com.group5.lingual.dso.flashcards.WrongFlashcardFake;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlashcardFactoryTest
{
    //Register and create an instance of a class that implements IFlashcard and has the appropriate 2-integer constructor
    @Test
    public void createProperFlashcard()
    {
        //Attempting to create a flashcard of an unregistered type should result in an exception
        assertThrows(IllegalArgumentException.class, () -> FlashcardFactory.createFlashcard(1, 1, "qData", "aData", "ProperFlashcardFake"));

        //Register the flashcard, create an instance, and ensure the instance is correct
        FlashcardFactory.registerFlashcardType(ProperFlashcardFake.class);
        IFlashcard card = FlashcardFactory.createFlashcard(1, 1, "qData", "aData", "ProperFlashcardFake");
        assertEquals("qData", card.getQuestion());
        assertEquals("aData", card.getAnswer());

        //Attempting to create an instance with an invalid flashcard ID should result in an exception
        assertThrows(IllegalArgumentException.class, () -> FlashcardFactory.createFlashcard(-1, 1, "qData", "aData", "ProperFlashcardFake"));
    }

    //Attempt to register and create an instance of a class that implements IFlashcard, but lacks the appropriate 2-integer constructor
    @Test
    public void createWrongConstructorFlashcard()
    {
        //Registering that class should succeed, but attempting to create an instance should result in an exception
        FlashcardFactory.registerFlashcardType(WrongFlashcardFake.class);
        assertThrows(IllegalStateException.class, () -> FlashcardFactory.createFlashcard(-1, 1, "qData", "aData", "WrongFlashcardFake"));
    }
}