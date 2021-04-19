package com.group5.lingual.data.fakedb;

import com.group5.lingual.dso.languages.ILanguage;
import com.group5.lingual.dso.languages.Language;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FakeLanguageDBTest
{
    @Test
    public void getLanguage()
    {
        FakeLanguageDB db = new FakeLanguageDB();

        //Add two languages to the database
        ILanguage l1 = new Language(1, "Lang1", "icon");
        ILanguage l2 = new Language(2, "Lang2", "icon");
        db.addLanguage(l1);
        db.addLanguage(l2);

        //Retrieve the languages using their IDs
        assertEquals(l1, db.getLanguage(1));
        assertEquals(l2, db.getLanguage(2));

        //Retrieving a nonexistent language should throw exception
        assertThrows(IllegalArgumentException.class, () -> db.getLanguage(3));
    }

    @Test
    public void getAllIDs()
    {
        FakeLanguageDB db = new FakeLanguageDB();

        //Getting all IDs from an empty database should return an empty list
        assertTrue(db.getAllIDs().isEmpty());

        //Add three languages to the database
        ILanguage l1 = new Language(1, "Lang1", "icon");
        ILanguage l2 = new Language(2, "Lang2", "icon");
        ILanguage l3 = new Language(3, "Lang3", "icon");
        db.addLanguage(l1);
        db.addLanguage(l2);
        db.addLanguage(l3);

        List<Integer> idList = db.getAllIDs();

        //Ensure that the list is composed of 1, 2, and 3
        assertEquals(3, idList.size());
        assertTrue(idList.contains(1));
        assertTrue(idList.contains(2));
        assertTrue(idList.contains(3));
        assertFalse(idList.contains(4));
    }

    @Test
    public void languageCount()
    {
        //An empty database has zero languages
        FakeLanguageDB db = new FakeLanguageDB();
        assertEquals(0, db.languageCount());

        //Adding one brings the count to one
        db.addLanguage(new Language(1, "L", "icon"));
        assertEquals(1, db.languageCount());

        //Adding one brings the count to two
        db.addLanguage(new Language(2, "L", "icon"));
        assertEquals(2, db.languageCount());
    }

    @Test
    public void addRemoveLanguage()
    {
        FakeLanguageDB db = new FakeLanguageDB();

        //A new database should be empty
        assertEquals(0, db.languageCount());
        assertThrows(IllegalArgumentException.class, () -> db.getLanguage(1));

        //Adding a language should now increase the size and allow us to retrieve the language
        ILanguage l = new Language(1, "L1", "icon");
        db.addLanguage(l);
        assertEquals(1, db.languageCount());
        assertEquals(l, db.getLanguage(1));

        //Adding a new language with the same ID should throw an IllegalArgumentException,
        ILanguage lSame = new Language(1, "LSameID", "icon");
        assertThrows(IllegalArgumentException.class, () -> db.addLanguage(lSame));

        //Adding another one with a different ID should not
        ILanguage lDifferent = new Language(2, "LDifferentID", "icon");
        db.addLanguage(lDifferent);
        assertEquals(2, db.languageCount());
        assertEquals(lDifferent, db.getLanguage(2));

        //Removing them should make them irretrievable
        db.removeLanguage(l);
        db.removeLanguage(lDifferent);
        assertThrows(IllegalArgumentException.class, () -> db.getLanguage(1));
        assertThrows(IllegalArgumentException.class, () -> db.getLanguage(2));
    }
}