package com.group5.lingual.dso.languages;

//An interface for the description of a language available in the app
public interface ILanguage
{
    public int getID();             //Unique language ID number, must be positive
    public String getName();        //Name of the language to display
    public String getIconName();    //Name of the drawable icon to display
    public String getCredits();     //List of sources from which this language course's content has been created

    //Generate a URL for an online English-This Language dictionary lookup of a given word
    //If the language has no set dictionary URL, use Google Translate by default
    public String getDictionaryURL(String word);
}