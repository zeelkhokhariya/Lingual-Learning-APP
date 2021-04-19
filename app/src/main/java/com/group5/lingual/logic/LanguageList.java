package com.group5.lingual.logic;

import com.group5.lingual.data.DBManager;
import com.group5.lingual.data.ILanguageDB;
import com.group5.lingual.dso.languages.ILanguage;

import java.util.List;

//Provides access to all of the languages in the app
//All instances access and operate on the same persistent database by default,
//unless supplied with a different one in the constructor
public class LanguageList implements ILanguageList
{
    private ILanguageDB languageDB;

    public LanguageList()
    {
        languageDB = DBManager.getLanguageDB();
    }

    public LanguageList(ILanguageDB languageDB)
    {
        this.languageDB = languageDB;
    }

    //Retrieve a language given its language ID number
    //Throws exception if no such language is found
    @Override
    public ILanguage getLanguage(int languageID)
    {
        return languageDB.getLanguage(languageID);
    }

    //Get a list of the language IDs of all the languages in the database, in no guaranteed order
    @Override
    public List<Integer> getAllIDs()
    {
        return languageDB.getAllIDs();
    }

    //Counts the number of languages in the database
    @Override
    public int languageCount()
    {
        return languageDB.languageCount();
    }
}