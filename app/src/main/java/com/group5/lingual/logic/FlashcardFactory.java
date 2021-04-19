package com.group5.lingual.logic;

import com.group5.lingual.dso.flashcards.AlphabetFlashcard;
import com.group5.lingual.dso.flashcards.FrenchPrepositionFlashcard;
import com.group5.lingual.dso.flashcards.GrammarFlashcard;
import com.group5.lingual.dso.flashcards.IFlashcard;
import com.group5.lingual.dso.flashcards.LogogramFlashcard;
import com.group5.lingual.dso.flashcards.SimpleFlashcard;
import com.group5.lingual.dso.flashcards.VocabularyFlashcard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

//Creates flashcards from their encoding in the database
public class FlashcardFactory
{
    private static boolean initialRegistrations = false; //Whether the factory has performed its initial flashcard type registrations

    private static Map<String, Class<? extends IFlashcard>> flashcardTypes = new HashMap<String, Class<? extends IFlashcard>>(); //List of registered flashcard types

    //Given its two IDs, its encoded questionData and answerData, and the name of the type, create an instance of the desired flashcard type
    public static IFlashcard createFlashcard(int flashcardID, int languageID, String questionData, String answerData, String typeName)
    {
        if(!initialRegistrations) //Ensure the initial flashcard type registrations have already been performed
            initializeRegistrations();

        IFlashcard f; //The created flashcard

        //Get the type of the flashcard
        Class<? extends IFlashcard> flashcardType = flashcardTypes.get(typeName);
        if(flashcardType == null) //Check that the type was retrieved successfully
            throw new IllegalArgumentException("Flashcard type name " + typeName + " not registered");

        //Get the type's constructor and instantiate a flashcard using that constructor
        try
        {
            Constructor<? extends IFlashcard> constructor = flashcardType.getDeclaredConstructor(int.class, int.class);
            f = constructor.newInstance(flashcardID, languageID);
        }
        catch(NoSuchMethodException e)
        {
            throw new IllegalStateException("Flashcard type name " + typeName + " registered to class without appropriate constructor", e);
        }
        catch(IllegalAccessException | InstantiationException | InvocationTargetException e)
        {
            throw new IllegalArgumentException("Failed to instantiate flashcard", e);
        }

        f.initializeContent(questionData, answerData);

        return f;
    }

    //Add a flashcard type to the list for future instantiation
    public static void registerFlashcardType(Class<? extends IFlashcard> type)
    {
        //Get the name of the type
        String typeName = type.getName();
        typeName = typeName.substring(typeName.lastIndexOf('.') + 1);

        flashcardTypes.put(typeName, type);
    }

    //Register all of the flashcard types
    private static void initializeRegistrations()
    {
        registerFlashcardType(SimpleFlashcard.class);
        registerFlashcardType(AlphabetFlashcard.class);
        registerFlashcardType(VocabularyFlashcard.class);
        registerFlashcardType(LogogramFlashcard.class);
        registerFlashcardType(GrammarFlashcard.class);
        registerFlashcardType(FrenchPrepositionFlashcard.class);
        initialRegistrations = true;
    }
}
