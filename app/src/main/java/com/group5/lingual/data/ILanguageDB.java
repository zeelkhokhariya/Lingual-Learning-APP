package com.group5.lingual.data;

import com.group5.lingual.dso.languages.ILanguage;

import java.util.List;

//Holds and provides access to a collection of languages
public interface ILanguageDB
{
    //Retrieve a language given its language ID number
    //Throws an exception if no such language is found
    public ILanguage getLanguage(int languageID);

    //Get a list of the language IDs of all the languages in the database, in no guaranteed order
    public List<Integer> getAllIDs();

    //Counts the number of languages in the database
    public int languageCount();

    //Adds a language to the database
    //The new language must not share its Language ID with any other language in the database
    public void addLanguage(ILanguage l);

    //Remove a language from the database
    //Returns the removed language if successful, throws exception otherwise
    public ILanguage removeLanguage(ILanguage l);
}