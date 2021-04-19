package com.group5.lingual.data.fakedb;

import com.group5.lingual.data.ILanguageDB;
import com.group5.lingual.dso.languages.ILanguage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Holds and provides access to a collection of languages
//A fake implementation of a language database using Lists
public class FakeLanguageDB implements ILanguageDB
{
    private List<ILanguage> languages;    //List of languages in the database
    private Set<Integer> usedIDs;           //Record of the Language IDs of all the languages in the database

    public FakeLanguageDB()
    {
        languages = new ArrayList<ILanguage>();
        usedIDs = new HashSet<Integer>();
    }

    //Retrieves a language given its ID, throws exception if not found
    @Override
    public ILanguage getLanguage(int languageID)
    {
        for(ILanguage l : languages)
            if(l.getID() == languageID)
                return l;

        throw new IllegalArgumentException("Could not find language with Language ID " + languageID);
    }

    //Gets a list of the language IDs of all the languages in the database, in no guaranteed order
    @Override
    public List<Integer> getAllIDs() { return new ArrayList<Integer>(usedIDs); }

    //Counts the number of languages in the database
    @Override
    public int languageCount()
    {
        return languages.size();
    }

    //Adds a language to the database
    //The new language must not share its Language ID with any other language in the database
    @Override
    public void addLanguage(ILanguage l)
    {
        //Check for duplicate ID
        if(usedIDs.contains(l.getID()))
            throw new IllegalArgumentException("Database already contains language with ID " + l.getID());

        languages.add(l);
        usedIDs.add(l.getID());
    }

    //Remove a language from the database
    //Returns the removed language if successful, throws exception otherwise
    @Override
    public ILanguage removeLanguage(ILanguage l)
    {
        if(languages.remove(l))
        {
            usedIDs.remove(l.getID());
            return l;
        }
        else
            throw new IllegalArgumentException("Could not find language to remove");
    }
}