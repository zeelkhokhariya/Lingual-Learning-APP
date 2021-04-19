package com.group5.lingual.dso.languages;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class LanguageTest
{
    @Test
    public void getID()
    {
        ILanguage l = new Language(123, "Lang", "icon");
        assertEquals(123, l.getID());
    }

    @Test
    public void getName()
    {
        ILanguage l = new Language(1, "LangName", "icon");
        assertEquals("LangName", l.getName());
    }

    @Test
    public void getIconName()
    {
        ILanguage l = new Language(1, "Lang", "icon123");
        assertEquals("icon123", l.getIconName());
    }

    //Should throw IllegalArgumentException when trying to create a language with a non-positive ID
    @Test
    public void invalidID()
    {
        assertThrows(IllegalArgumentException.class, () -> new Language(0, "Lang", "icon"));
        assertThrows(IllegalArgumentException.class, () -> new Language(-1, "Lang", "icon"));

        assertThrows(IllegalArgumentException.class, () -> new Language(0, "Lang", "icon", "dictionary.ca"));
        assertThrows(IllegalArgumentException.class, () -> new Language(-1, "Lang", "icon", "dictionary.ca"));

        assertThrows(IllegalArgumentException.class, () -> new Language(0, "Lang", "icon", "dictionary.ca", "test credits"));
        assertThrows(IllegalArgumentException.class, () -> new Language(-1, "Lang", "icon", "dictionary.ca", "test credits"));
    }

    @Test
    public void getCredits()
    {
        //Specify credits and ensure the result is correct
        String testCredits = "Language data from some source";
        ILanguage l1 = new Language(1, "Lang", "icon123", "dictionary.ca", testCredits);
        assertEquals(testCredits, l1.getCredits());

        //Do not specify credit sand ensure the default is set
        String defaultCredits = "No credits for this course.";
        ILanguage l2 = new Language(2, "Lang", "icon123");
        assertEquals(defaultCredits, l2.getCredits());
        ILanguage l3 = new Language(3, "Lang", "icon123", "dictionary.ca");
        assertEquals(defaultCredits, l3.getCredits());
    }

    @Test
    public void getDictionaryURL()
    {
        //Specify a dictionary URL and ensure the result is correct
        String testWord = "testing";
        String testURLTemplate = "https://dictionary.ca/search/" + Language.getURLPlaceholder() + "/testing";
        String expectedURL = "https://dictionary.ca/search/" + testWord + "/testing";
        ILanguage l1 = new Language(1, "Lang", "icon123", testURLTemplate, "test credits");
        assertEquals(expectedURL, l1.getDictionaryURL(testWord));
        ILanguage l2 = new Language(2, "Lang", "icon123", testURLTemplate);
        assertEquals(expectedURL, l2.getDictionaryURL(testWord));

        String defaultURL = "https://translate.google.com/?sl=auto&tl=en&text=" + testWord + "&op=translate";
        ILanguage l3 = new Language(3, "Lang", "icon123");
        assertEquals(defaultURL, l3.getDictionaryURL(testWord));
    }

    @Test
    public void equals()
    {
        //Test l1 against several objects, one equal and one different in each possible way
        ILanguage l1 = new Language(1, "Lang", "icon123", "dictionary.ca", "test credits");
        assertEquals(l1, new Language(1, "Lang", "icon123", "dictionary.ca", "test credits"));
        assertNotEquals(l1, new Language(2, "Lang", "icon123", "dictionary.ca", "test credits"));
        assertNotEquals(l1, new Language(1, "Mang", "icon123", "dictionary.ca", "test credits"));
        assertNotEquals(l1, new Language(1, "Lang", "jcon123", "dictionary.ca", "test credits"));
        assertNotEquals(l1, new Language(1, "Lang", "icon123", "eictionary.ca", "test credits"));
        assertNotEquals(l1, new Language(1, "Lang", "icon123", "eictionary.ca", "uest credits"));
        assertNotEquals(l1, null);
        assertNotEquals(l1, 1);
    }
}