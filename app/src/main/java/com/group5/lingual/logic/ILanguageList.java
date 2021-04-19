package com.group5.lingual.logic;

import com.group5.lingual.dso.languages.ILanguage;

import java.util.List;

//Provides access to all of the languages in the app
//All instances access and operate on the same persistent database
public interface ILanguageList
{
    //Retrieve a language given its language ID number
    //Throws exception if no such language is found
    public ILanguage getLanguage(int languageID);

    //Get a list of the language IDs of all the languages in the database, in no guaranteed order
    public List<Integer> getAllIDs();

    //Counts the number of languages in the database
    public int languageCount();
}