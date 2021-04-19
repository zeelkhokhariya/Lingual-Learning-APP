package com.group5.lingual.data.hsqldb;

import com.group5.lingual.data.IFlashcardDB;
import com.group5.lingual.dso.flashcards.IFlashcard;
import com.group5.lingual.logic.FlashcardFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

//A flashcard database deck implemented in HSQLDB
//Flashcards are held in a sequence in ascending order of their due date
public class HSQLFlashcardDB extends HSQLDatabase implements IFlashcardDB
{
    private final String tagsTable; //Name of the HSQLDB table relating flashcards to tag strings

    public HSQLFlashcardDB(String dbPath, String tableName, String tagsTable)
    {
        super(dbPath, tableName);

        this.tagsTable = tagsTable;
    }

    //Get a flashcard in the database given its ID
    //Throws exception if no such flashcard is found
    @Override
    public IFlashcard getFlashcard(int flashcardID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName + " WHERE flashcardID = ?");
            st.setInt(1, flashcardID);
            ResultSet rs = st.executeQuery();

            //Construct a flashcard from the first result, or throw an exception if none were found
            if(!rs.next())
                throw new IllegalArgumentException("Could not find flashcard with Flashcard ID " + flashcardID + " in table " + tableName);
            IFlashcard f = flashcardFromResultSet(rs);

            //Ensure that only one result was retrieved from the database
            if(rs.next())
                throw new IllegalStateException("Flashcard database " + tableName + " retrieved multiple flashcards for a given ID");

            return f;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Gets the first card in the sequence that matches the given language and tags
    //and whose due date is earlier than or equal to the given cutoff date (in milliseconds)
    //Returns null if there are no such cards in the database
    @Override
    public IFlashcard getFirstMatchingCard(int languageID, Collection<String> tags, long cutoffDate)
    {
        try(Connection c = connection())
        {
            ResultSet rs; //The results of the coming query
            if(tags.size() > 0) //If tag search necessary, perform a complex query
            {
                //Assemble the list of tags to search for in SQL format
                String tagList = "";
                for(String tag : tags)
                    tagList += "'" + tag + "',";
                tagList = tagList.substring(0, tagList.length() - 1); //Remove final ","

                //Construct and execute the SQL select statement
                //Gets one of the flashcards which matches the language ID and is associated with all of the tags
                PreparedStatement st= c.prepareStatement("SELECT f.* FROM " + tableName+ " f" +
                        " INNER JOIN " + tagsTable + " t ON f.flashcardID = t.flashcardID" +
                        " WHERE t.tag in (" + tagList + ") GROUP BY f.flashcardID" +
                        " HAVING COUNT(DISTINCT t.tag) = ? AND f.languageID = ? AND f.dueDate <= ?" +
                        " ORDER BY dueDate LIMIT 1");
                st.setInt(1, tags.size());
                st.setInt(2, languageID);
                st.setTimestamp(3, new Timestamp(cutoffDate));
                rs = st.executeQuery();
            }
            else //Otherwise perform a simple query
            {
                //Construct and execute the SQL select statement
                PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName +
                        " WHERE languageID = ? AND dueDate <= ? ORDER BY dueDate LIMIT 1");
                st.setInt(1, languageID);
                st.setTimestamp(2, new Timestamp(cutoffDate));
                rs = st.executeQuery();
            }

            //Create a flashcard from the result if any record was retrieved, otherwise leave it null
            IFlashcard f = null;
            if(rs.next())
                f = flashcardFromResultSet(rs);

            return f;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Gets the number of cards in the database that match the given language and tags
    //and whose due date is earlier than or equal to the given cutoff date (in milliseconds)
    @Override
    public int cardCount(int languageID, Collection<String> tags, long cutoffDate)
    {
        try(Connection c = connection())
        {
            ResultSet rs; //The results of the coming query
            if(tags.size() > 0) //If tag search necessary, perform a complex query
            {
                //Assemble the list of tags to search for in SQL format
                String tagList = "";
                for(String tag : tags)
                    tagList += "'" + tag + "', ";
                tagList = tagList.substring(0, tagList.length() - 1); //Remove final ","

                //Construct and execute the SQL select statement
                //Gets all of the flashcard IDs which match the language ID and are associated with all of the tags
                PreparedStatement st= c.prepareStatement("SELECT f.flashcardID FROM " + tableName+ " f" +
                        " INNER JOIN " + tagsTable + " t ON f.flashcardID = t.flashcardID" +
                        " WHERE t.tag in (" + tagList + ") GROUP BY f.flashcardID" +
                        " HAVING COUNT(DISTINCT t.tag) = ? AND f.languageID = ? AND f.dueDate <= ?");
                st.setInt(1, tags.size());
                st.setInt(2, languageID);
                st.setTimestamp(3, new Timestamp(cutoffDate));
                rs = st.executeQuery();
            }
            else //Otherwise perform a simple query
            {
                //Construct and execute the SQL select statement
                PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName + " WHERE languageID = ? AND dueDate <= ?");
                st.setInt(1, languageID);
                st.setTimestamp(2, new Timestamp(cutoffDate));
                rs = st.executeQuery();
            }

            //Count the results
            int count = 0;
            while(rs.next())
                count++;

            return count;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Add a new card to the database, setting its due date and user's correct streak as given
    //The new card must not share its Flashcard ID with any other card in the database
    @Override
    public void addCard(IFlashcard f, long dueDate, int streak)
    {
        try(Connection c = connection())
        {
            //Get flashcard type name
            String typeName = f.getClass().getName();
            typeName = typeName.substring(typeName.lastIndexOf('.') + 1);

            //Construct and execute the SQL insert statement
            PreparedStatement st = c.prepareStatement("INSERT INTO " + tableName + " VALUES(?, ?, ?, ?, ?, ?, ?)");
            st.setInt(1, f.getID());
            st.setInt(2, f.getLanguageID());
            st.setTimestamp(3, new Timestamp(dueDate));
            st.setInt(4, streak);
            st.setString(5, typeName);
            st.setString(6, f.getQuestionData());
            st.setString(7, f.getAnswerData());
            st.executeUpdate();

            //Add the tags stored in the flashcard to the flashcard-tags database
            for(String tag : f.getAllTags())
            {
                PreparedStatement tagSt = c.prepareStatement("INSERT INTO " + tagsTable + " VALUES(?, ?)");
                tagSt.setInt(1, f.getID());
                tagSt.setString(2, tag);
                tagSt.executeUpdate();
            }
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Remove a card from the database
    //Returns the removed card if successful, throws exception otherwise
    @Override
    public IFlashcard removeCard(IFlashcard f)
    {
        //First ensure that the flashcard is in the database
        getFlashcard(f.getID());

        try(Connection c = connection())
        {
            //Construct and execute the SQL delete statement
            PreparedStatement st = c.prepareStatement("DELETE FROM " + tableName + " WHERE flashcardID = ?");
            st.setInt(1, f.getID());
            st.executeUpdate();

            //Delete the associated tags as well
            PreparedStatement tagsSt = c.prepareStatement("DELETE FROM " + tagsTable + " WHERE flashcardID = ?");
            tagsSt.setInt(1, f.getID());
            tagsSt.executeUpdate();

            return f;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Gets the current streak of successive correct answers the user has given for a given card
    //Throws exception if no such flashcard is found
    @Override
    public int getStreak(int flashcardID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT streak FROM " + tableName + " WHERE flashcardID = ?");
            st.setInt(1, flashcardID);
            ResultSet rs = st.executeQuery();

            //Construct a flashcard from the first result, or throw an exception if none were found
            if(!rs.next())
                throw new IllegalArgumentException("Could not find flashcard with Flashcard ID " + flashcardID + " in table " + tableName);
            int streak = rs.getInt("streak");

            //Ensure that only one result was retrieved from the database
            if(rs.next())
                throw new IllegalStateException("Flashcard database " + tableName + " retrieved multiple flashcards for a given ID");

            return streak;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Gets a flashcard's due date
    //Throws exception if no such flashcard is found
    @Override
    public long getDueDate(int flashcardID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT dueDate FROM " + tableName + " WHERE flashcardID = ?");
            st.setInt(1, flashcardID);
            ResultSet rs = st.executeQuery();

            //Construct a flashcard from the first result, or throw an exception if none were found
            if(!rs.next())
                throw new IllegalArgumentException("Could not find flashcard with Flashcard ID " + flashcardID + " in table " + tableName);
            long date = rs.getTimestamp("dueDate").getTime();

            //Ensure that only one result was retrieved from the database
            if(rs.next())
                throw new IllegalStateException("Flashcard database " + tableName + " retrieved multiple flashcards for a given ID");

            return date;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Reschedules a flashcard by setting a new due date for it
    //If the rescheduling is due to a correct answer on the card, increment the card's streak
    //If the rescheduling was for any other reason, reset the card's streak to zero
    //Throws exception if no such flashcard is found
    @Override
    public void rescheduleCard(int flashcardID, long newDueDate, boolean answeredCorrectly)
    {
        //First ensure that the flashcard is in the database
        getFlashcard(flashcardID);

        try(Connection c = connection())
        {
            //Construct and execute the SQL update
            PreparedStatement st = c.prepareStatement("UPDATE " + tableName + " SET dueDate = ?, streak = ? WHERE flashcardID = ?");
            st.setTimestamp(1, new Timestamp(newDueDate));
            st.setInt(2, answeredCorrectly ? (getStreak(flashcardID) + 1) : 0);
            st.setInt(3, flashcardID);
            st.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Creates a flashcard from an SQL result set
    //Uses the typeName field to decode the questionData and answerData fields and create the appropriate subclass
    private IFlashcard flashcardFromResultSet(ResultSet rs) throws SQLException
    {
        //Create the flashcard from the query data
        IFlashcard f = FlashcardFactory.createFlashcard(rs.getInt("flashcardID"), rs.getInt("languageID"),
                rs.getString("questionData"), rs.getString("answerData"), rs.getString("typeName"));

        //Query all of the tags associated with this flashcard and add them to the card
        try(Connection c = connection())
        {
            PreparedStatement tagSt = c.prepareStatement("SELECT tag FROM " + tagsTable + " WHERE flashcardID = ?");
            tagSt.setInt(1, f.getID());
            ResultSet tagRs = tagSt.executeQuery();
            while (tagRs.next())
                f.addTag(tagRs.getString("tag"));
        }

        return f;
    }
}