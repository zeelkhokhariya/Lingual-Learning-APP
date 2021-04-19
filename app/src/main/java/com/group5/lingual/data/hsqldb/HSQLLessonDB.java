package com.group5.lingual.data.hsqldb;

import com.group5.lingual.data.ILessonDB;
import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.ILessonModule;
import com.group5.lingual.dso.lessons.ILessonSummary;
import com.group5.lingual.dso.lessons.Lesson;
import com.group5.lingual.logic.LessonModuleFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//A lesson database implemented in HSQLDB
public class HSQLLessonDB extends HSQLDatabase implements ILessonDB
{
    private final String modulesTable;      //Name of the HSQLDB table relating modules to lessons
    private final String flashcardsTable;   //Name of the HSQLDB table relating lessons to the flashcard IDs they unlock

    public HSQLLessonDB(String dbPath, String tableName, String modulesTable, String flashcardsTable)
    {
        super(dbPath, tableName);

        this.modulesTable = modulesTable;
        this.flashcardsTable = flashcardsTable;
    }

    //Retrieve a lesson given its lesson ID number
    //Throws an exception if no such lesson is found
    @Override
    public ILesson getLesson(int lessonID)
    {
        //In this implementation, the lesson summary is a Lesson without its modules
        Lesson l = (Lesson)getLessonSummary(lessonID);

        //Query all of the content modules associated with this lesson and add them to it
        try(Connection c = connection())
        {
            //Query the related modules
            PreparedStatement st = c.prepareStatement("SELECT * FROM " + modulesTable + " WHERE lessonID = ? ORDER BY moduleIndex");
            st.setInt(1, l.getID());
            ResultSet modulesRs = st.executeQuery();

            //Add them to the lesson
            for(ILessonModule m : moduleFromResultSet(modulesRs))
                l.addModule(m);
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }

        return l;
    }

    //Retrieves a summary of a lesson given its lesson ID number
    //Throws an exception if no such lesson is found
    //In this implementation, this retrieves a lesson from the database but does not retrieve its content modules
    @Override
    public ILessonSummary getLessonSummary(int lessonID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName + " WHERE lessonID = ?");
            st.setInt(1, lessonID);
            ResultSet rs = st.executeQuery();

            //Construct a lesson from the first result, or throw an exception if none were found
            if(!rs.next())
                throw new IllegalArgumentException("Could not find lesson with Lesson ID " + lessonID);
            ILesson l = lessonFromResultSet(rs);

            //Ensure that only one result was retrieved from the database
            if(rs.next())
                throw new IllegalStateException("Lesson database retrieved multiple lesson for a given ID");

            return l;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Get a list of the lesson IDs of all the lessons in the database for a given language, in no guaranteed order
    @Override
    public List<Integer> getAllIDs(int languageID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT lessonID FROM " + tableName + " WHERE languageID = ?");
            st.setInt(1, languageID);
            ResultSet rs = st.executeQuery();

            //Add all of the resulting IDs to a list
            List<Integer> lessonIDs = new ArrayList<Integer>();
            while(rs.next())
                lessonIDs.add(rs.getInt("lessonID"));

            return lessonIDs;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Counts the number of lessons in the database for a given language
    @Override
    public int lessonCount(int languageID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT COUNT(*) AS lessonCount FROM " + tableName + " WHERE languageID = ?");
            st.setInt(1, languageID);
            ResultSet rs = st.executeQuery();

            //Return the count retrieved
            rs.next();
            return rs.getInt("lessonCount");
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Counts the number of unlocked lessons in the database for a given language
    @Override
    public int unlockedLessonCount(int languageID)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL select statement
            PreparedStatement st = c.prepareStatement("SELECT prerequisiteID FROM " + tableName + " WHERE languageID = ?");
            st.setInt(1, languageID);
            ResultSet rs = st.executeQuery();

            //Check how many of the prerequisite IDs have been completed
            int count = 0;
            while(rs.next())
            {
                int id = rs.getInt("prerequisiteID");
                if(id < 0 || getLessonCompletion(id))
                    count++;
            }
            return count;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Adds a lesson type to the database and marks whether it has been completed
    //The new lesson must not share its lesson ID with any other lesson in the database
    @Override
    public void addLesson(ILesson l, boolean completed)
    {
        try(Connection c = connection())
        {
            //Construct and execute the SQL insert statement
            PreparedStatement st = c.prepareStatement("INSERT INTO " + tableName + " VALUES(?, ?, ?, ?, ?, ?, ?)");
            st.setInt(1, l.getID());
            st.setInt(2, l.getLanguageID());
            st.setString(3, l.getName());
            st.setInt(4, l.getDuration());
            st.setString(5, l.getIconName());
            st.setInt(6, l.getPrerequisiteID());
            st.setBoolean(7, completed);
            st.executeUpdate();

            //Add the lesson's modules to the database
            int moduleIndex = 1; //The module's position within the lesson
            for(ILessonModule m : l.getModules())
            {
                //Get module type name
                String typeName = m.getClass().getName();
                typeName = typeName.substring(typeName.lastIndexOf('.') + 1);

                //Construct and execute the SQL insert statement
                PreparedStatement modSt = c.prepareStatement("INSERT INTO " + modulesTable + " VALUES(?, ?, ?, ?)");
                modSt.setInt(1, l.getID());
                modSt.setInt(2, moduleIndex++);
                modSt.setString(3, typeName);
                modSt.setString(4, m.getContentData());
                modSt.executeUpdate();
            }

            //Add the lesson's tied flashcards to the database
            for(int fID : l.getTiedFlashcards())
            {
                PreparedStatement fSt = c.prepareStatement("INSERT INTO " + flashcardsTable + " VALUES(?, ?)");
                fSt.setInt(1, l.getID());
                fSt.setInt(2, fID);
            }
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Remove a lesson from the database
    //Returns the removed lesson if successful, throws exception otherwise
    @Override
    public ILesson removeLesson(ILesson l)
    {
        //First ensure that the lesson is in the database
        getLesson(l.getID());

        try(Connection c = connection())
        {
            //Construct and execute the SQL delete statement
            PreparedStatement st = c.prepareStatement("DELETE FROM " + tableName + " WHERE lessonID = ?");
            st.setInt(1, l.getID());
            st.executeUpdate();

            return l;
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Creates a lesson from an SQL result set
    private Lesson lessonFromResultSet(ResultSet rs) throws SQLException
    {
        //First get the flashcard IDs tied to this lesson
        List<Integer> tiedFlashcards = new ArrayList<Integer>();
        try(Connection c = connection())
        {
            PreparedStatement st = c.prepareStatement("SELECT flashcardID FROM " + flashcardsTable + " WHERE lessonID = ?");
            st.setInt(1, rs.getInt("lessonID"));
            ResultSet flashcards = st.executeQuery();

            while(flashcards.next())
                tiedFlashcards.add(flashcards.getInt("flashcardID"));
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }

        //Create the lesson
        return new Lesson(rs.getInt("lessonID"), rs.getInt("languageID"),
                rs.getString("name"), rs.getInt("duration"), rs.getString("iconName"),
                rs.getInt("prerequisiteID"), tiedFlashcards);
    }

    //Creates a list of lesson content modules from an SQL result set
    private List<ILessonModule> moduleFromResultSet(ResultSet rs) throws SQLException
    {
        List<ILessonModule> modules = new ArrayList<ILessonModule>();

        //Create the modules from their content data
        while(rs.next())
            modules.add(LessonModuleFactory.createLessonModule(rs.getString("contentData"), rs.getString("typeName")));

        return modules;
    }

    //Getter for whether a lesson should be marked as completed
    @Override
    public boolean getLessonCompletion(int lessonID)
    {
        try(Connection c = connection())
        {
            PreparedStatement st = c.prepareStatement("SELECT completed FROM " + tableName + " WHERE lessonID = ?");
            st.setInt(1, lessonID);
            ResultSet rs = st.executeQuery();

            if(!rs.next())
                throw new IllegalArgumentException("Could not find lesson with Lesson ID " + lessonID);
            else
                return rs.getBoolean("completed");
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Setter for whether a lesson should be marked as completed
    @Override
    public void setLessonCompletion(int lessonID, boolean completed)
    {
        try(Connection c = connection())
        {
            PreparedStatement st = c.prepareStatement("UPDATE " + tableName + " SET completed = ? WHERE lessonID = ?");
            st.setBoolean(1, completed);
            st.setInt(2, lessonID);
            st.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }
}
