package com.group5.lingual.dso.flashcards;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//A flashcard containing a question and answer for the user to test themselves with
//A flashcard can be tagged with strings for categorization and filtration purposes
public abstract class Flashcard implements IFlashcard
{
    //Separator to allow multiple strings to be stored in the questionData and answerData columns of the database
    //(ex. questionData column contains 'stringA~~~stringB~~~stringC')
    protected static final String DB_CONTENT_SEPARATOR = "~~~";

    protected boolean contentInitialized = false; //Whether the flashcard's content has yet been initialized

    private int id;                 //Unique flashcard ID number, must be positive
    private int languageID;         //The ID number of the Language this card is associated with

    private Set<String> tags;       //Tags to help organize and search for cards

    public Flashcard(int flashcardID, int languageID)
    {
        //Validate IDs
        if(flashcardID <= 0)
            throw new IllegalArgumentException("Flashcard ID must be positive");
        if(languageID <= 0)
            throw new IllegalArgumentException("Language ID must be positive");

        this.id = flashcardID;
        this.languageID = languageID;
        tags = new HashSet<>();
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        //Ensure the other is a flashcard
        if(!(obj instanceof Flashcard))
            return false;

        Flashcard other = (Flashcard)obj;

        //Compare values
        return getID() == other.getID()
                && getLanguageID() == (other.getLanguageID())
                && getAllTags().equals(other.getAllTags())
                && getQuestion().equals(other.getQuestion())
                && getAnswer().equals(other.getAnswer());
    }

    //Getter for the flashcard ID number and associated language ID number
    @Override public int getID() { return id; }
    @Override public int getLanguageID() { return languageID; }

    //Add, remove, and check for tags assigned to this card
    @Override public void addTag(String tag) { tags.add(tag); }
    @Override public void removeTag(String tag) { tags.remove(tag); }
    @Override public boolean hasTag(String tag) { return tags.contains(tag); }
    @Override public boolean hasTags(Collection<String> tags) { return this.tags.containsAll(tags); }

    //Get the full collection of tags on this card
    @Override public Collection<String> getAllTags() { return tags; }
}