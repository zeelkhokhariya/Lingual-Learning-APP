package com.group5.lingual.dso.languages;

import androidx.annotation.Nullable;

//Describes a language available in the app
public class Language implements ILanguage
{
    //Placeholder for the word to be looked up within a dictionary URL template
    private static final String URL_PLACEHOLDER = "~~~";

    //The URL template for a Google Translate lookup, used as default if no template URL is supplied
    private static final String GOOGLE_TRANSLATE_URL = "https://translate.google.com/?sl=auto&tl=en&text=" + URL_PLACEHOLDER + "&op=translate";

    private int id;             //Unique language ID number, must be positive
    private String name;        //Name of the language to display
    private String iconName;    //Name of the drawable icon to display
    private String credits;     //List of sources from which this language course's content has been created

    //A URL template for an English-This Language dictionary
    //By replacing URL_PLACEHOLDER with a given word, it forms the URL for a lookup of that word
    private String dictionaryURL;

    public Language(int id, String name, String iconName)
    {
        //Validate ID
        if(id <= 0)
            throw new IllegalArgumentException("Language ID must be positive");

        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.credits = "";

        this.dictionaryURL = "";
    }

    public Language(int id, String name, String iconName, String dictionaryURL)
    {
        //Validate ID
        if(id <= 0)
            throw new IllegalArgumentException("Language ID must be positive");

        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.credits = "";

        this.dictionaryURL = dictionaryURL;
    }

    public Language(int id, String name, String iconName, String dictionaryURL, String credits)
    {
        //Validate ID
        if(id <= 0)
            throw new IllegalArgumentException("Language ID must be positive");

        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.credits = credits;

        this.dictionaryURL = dictionaryURL;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        //Ensure the other is a language
        if(!(obj instanceof Language))
            return false;

        Language other = (Language)obj;

        //Compare values
        return getID() == other.getID()
                && getName().equals(other.getName())
                && getIconName().equals(other.getIconName())
                && getCredits().equals(other.getCredits())
                && getDictionaryURL("test").equals(other.getDictionaryURL("test"));
    }

    //Getter for the placeholder to be used in dictionary URL templates in place of the word to be looked up
    public static String getURLPlaceholder() { return URL_PLACEHOLDER; }

    //Basic getters for the language's data
    @Override public int getID() { return id; }
    @Override public String getName() { return name; }
    @Override public String getIconName() { return iconName; }
    @Override public String getCredits() { return credits.isEmpty() ? "No credits for this course." : credits; }

    //Generate a URL for an online English-This Language dictionary lookup of a given word
    //If the language has no set dictionary URL, use Google Translate by default
    @Override
    public String getDictionaryURL(String word)
    {
        return (dictionaryURL.isEmpty() ? GOOGLE_TRANSLATE_URL : dictionaryURL).replaceAll(URL_PLACEHOLDER, word);
    }
}