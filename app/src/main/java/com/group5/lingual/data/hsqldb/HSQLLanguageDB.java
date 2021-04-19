package com.group5.lingual.data.hsqldb;

import com.group5.lingual.data.ILanguageDB;
import com.group5.lingual.dso.languages.ILanguage;
import com.group5.lingual.dso.languages.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//A language database implemented in HSQLDB
public class HSQLLanguageDB extends HSQLDatabase implements ILanguageDB
{
    public HSQLLanguageDB(String dbPath, String tableName)
    {
        super(dbPath, tableName);
    }

    //Retrieve a language given its language ID number
    //Throws exception if no such language is found
    @Override
    public ILanguage getLanguage(int languageID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName + " WHERE languageID = ?");
            st.setInt(1, languageID);
            ResultSet rs = st.executeQuery();

            //Construct a language from the first result, or throw an exception if none were found
            if(!rs.next())
                throw new IllegalArgumentException("Could not find language with Language ID " + languageID);
            ILanguage l = languageFromResultSet(rs);

            //Ensure that only one result was retrieved from the database
            if(rs.next())
                throw new IllegalStateException("Language database retrieved multiple languages for a given ID");

            return l;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Get a list of the language IDs of all the languages in the database, in no guaranteed order
    @Override
    public List<Integer> getAllIDs()
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT languageID FROM " + tableName);
            ResultSet rs = st.executeQuery();

            //Add all of the resulting IDs to a list
            List<Integer> languageIDs = new ArrayList<Integer>();
            while(rs.next())
                languageIDs.add(rs.getInt("languageID"));

            return languageIDs;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Counts the number of languages in the database
    @Override
    public int languageCount()
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT COUNT(*) AS languageCount FROM " + tableName);
            ResultSet rs = st.executeQuery();

            //Return the count retrieved
            rs.next();
            return rs.getInt("languageCount");
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Adds a language to the database
    //The new language must not share its Language ID with any other language in the database
    @Override
    public void addLanguage(ILanguage l)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL insert statement
            PreparedStatement st = c.prepareStatement("INSERT INTO " + tableName + " VALUES(?, ?, ?, ?, ?)");
            st.setInt(1, l.getID());
            st.setString(2, l.getName());
            st.setString(3, l.getIconName());
            st.setString(4, l.getDictionaryURL(Language.getURLPlaceholder()));
            st.setString(5, l.getCredits());
            st.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Remove a language from the database
    //Returns the removed language if successful, throws exception otherwise
    @Override
    public ILanguage removeLanguage(ILanguage l)
    {
        //First ensure that the language is in the database
        getLanguage(l.getID());

        try(Connection c = connection())
        {
            //Construct and execute the SQL delete statement
            PreparedStatement st = c.prepareStatement("DELETE FROM " + tableName + " WHERE languageID = ?");
            st.setInt(1, l.getID());
            st.executeUpdate();

            return l;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Creates a language from an SQL result set
    private ILanguage languageFromResultSet(ResultSet rs) throws SQLException
    {
        return new Language(rs.getInt("languageID"), rs.getString("name"), rs.getString("iconName"), rs.getString("dictionaryURL"), rs.getString("credits"));
    }
}
