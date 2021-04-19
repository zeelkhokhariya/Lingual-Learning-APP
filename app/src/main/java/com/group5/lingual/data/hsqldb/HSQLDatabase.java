package com.group5.lingual.data.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//A database implemented using HSQLDB
public abstract class HSQLDatabase
{
    private final String dbPath;        //Path of the HSQL database this is tied to
    protected final String tableName;   //Name of the HSQLDB table this is tied to

    public HSQLDatabase(String dbPath, String tableName)
    {
        this.dbPath = dbPath;
        this.tableName = tableName;
    }

    //Establish a connection with the database, throwing an exception if it fails
    protected Connection connection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";", "SA", "");
    }
}