package com.group5.lingual.logic;

import com.group5.lingual.data.DBManager;
import com.group5.lingual.data.ILanguageDB;
import com.group5.lingual.dso.languages.ILanguage;
import com.group5.lingual.dso.languages.Language;
import com.group5.lingual.data.DBLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class LanguageListIntegrationTest
{
    ILanguageDB db;
    ILanguageList list;

    ILanguage l1 = new Language(1, "Lang1", "icon");
    ILanguage l2 = new Language(2, "Lang2", "icon");
    ILanguage l3 = new Language(3, "Lang3", "icon");

    Collection<String> tags1 = new ArrayList<String>();
    Collection<String> tags2 = new ArrayList<String>();

    @Before
    public void setUp()
    {
        DBManager.setDBPathName(DBLoader.loadDB());

        db = DBManager.getLanguageDB();
        list = new LanguageList();

        db.addLanguage(l1);
        db.addLanguage(l2);
    }

    @After
    public void tearDown()
    {
        DBManager.shutdownDB();
        DBLoader.cleanup();
    }

    @Test
    public void getLanguage()
    {
        assertEquals(l1, list.getLanguage(1));
        assertEquals(l2, list.getLanguage(2));
        assertThrows(IllegalArgumentException.class, () -> list.getLanguage(3));
    }

    @Test
    public void getAllIDs()
    {
        List<Integer> idList = list.getAllIDs();
        assertTrue(idList.contains(1));
        assertTrue(idList.contains(2));
        assertFalse(idList.contains(3));
        assertEquals(2, idList.size());
    }

    @Test
    public void languageCount()
    {
        assertEquals(2, list.languageCount());
        db.addLanguage(l3);
        assertEquals(3, list.languageCount());
    }
}