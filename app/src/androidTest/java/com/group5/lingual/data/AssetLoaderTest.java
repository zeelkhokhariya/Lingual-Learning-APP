package com.group5.lingual.data;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//Tests the loading of app assets such as the database
public class AssetLoaderTest
{
    @Test
    public void copyDatabaseToDevice()
    {
        Context context = ApplicationProvider.getApplicationContext();

        //Attempting to use the database should initially fail
        boolean dbFailed = false;
        try
        {
            DBManager.getLanguageDB();
        }
        catch(IllegalStateException e)
        {
            dbFailed = true;
        }
        assertTrue(dbFailed);

        //Copy the database from assets
        boolean copyException = false;
        try
        {
            AssetLoader.copyDatabaseToDevice(context, true);
        }
        catch(IOException e)
        {
            copyException = true;
        }
        assertFalse(copyException);

        //Now using the database should succeed
        dbFailed = false;
        try
        {
            dbFailed = DBManager.getLanguageDB() == null;
        }
        catch(IllegalStateException e)
        {
            dbFailed = true;
        }
        assertFalse(dbFailed);
    }
}