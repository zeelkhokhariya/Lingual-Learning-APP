package com.group5.lingual.logic;

import com.group5.lingual.data.DBManager;

//Performs the actions necessary to save any pending data,
//in order to ensure integrity in the event that the app terminates
public class SaveManager
{
    //Saves data by shutting down the database
    public static void save()
    {
        DBManager.shutdownDB();
    }
}
