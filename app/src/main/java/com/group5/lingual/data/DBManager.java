package com.group5.lingual.data;

import com.group5.lingual.data.fakedb.FakeDBFactory;
import com.group5.lingual.data.hsqldb.HSQLDBException;
import com.group5.lingual.data.hsqldb.HSQLFlashcardDB;
import com.group5.lingual.data.hsqldb.HSQLLanguageDB;
import com.group5.lingual.data.hsqldb.HSQLLessonDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

//Contains static that provide access to the database interface singletons
public class DBManager
{
    //The maximum value for a JDBC timestamp
    //Used as the due date for locked cards (effectively prevents them from being reviewed until rescheduled
    private static final long DATE_MAX = Timestamp.valueOf("9999-12-31 23:59:59").getTime(); //The maximum value of a JDBC timestamp

    private static final String DB_NAME = "LingualDB";  //Name of the app's database

    //Names of the HSQLDB tables
    private static final String LANGUAGE_TABLE_NAME = "languages";
    private static final String FLASHCARD_TABLE_NAME = "flashcards";
    private static final String FLASHCARD_TAGS_TABLE_NAME = "flashcardTags";
    private static final String LESSON_TABLE_NAME = "lessons";
    private static final String LESSON_MODULES_TABLE_NAME = "lessonModules";
    private static final String LESSON_FLASHCARDS_TABLE_NAME = "lessonFlashcards";

    private static String dbPath = "";          //Path of the app's database
    private static boolean initialized = false; //Whether the fake databases have been given content

    //Database singletons
    private static ILanguageDB languageDB;      //All languages in the app
    private static IFlashcardDB flashcardDB;    //All flashcards in the app
    private static ILessonDB lessonDB;          //All lessons in the app

    //Sets the path of the app's database, appended with dbName
    public static void setDBPathName(String path)
    {
        dbPath = path + "/" + DB_NAME;
    }

    //Shuts down the database, saving any pending changes
    public static void shutdownDB()
    {
        try(Connection c = DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";", "SA", ""))
        {
            PreparedStatement st = c.prepareStatement("SHUTDOWN");
            st.execute();
        }
        catch(SQLException e)
        {
            throw new HSQLDBException(e);
        }
    }

    //Getter for the maximum date value
    public static long getDateMax() { return DATE_MAX; }

    //Provides the language database singleton
    public static ILanguageDB getLanguageDB()
    {
        if(!initialized)
            initialize();

        return languageDB;
    }

    //Provides the language database singleton
    public static ILessonDB getLessonDB()
    {
        if(!initialized)
            initialize();

        return lessonDB;
    }


    //Provides the flashcard database singleton
    public static IFlashcardDB getFlashcardDB()
    {
        if(!initialized)
            initialize();

        return flashcardDB;
    }

    //Initialize the databases
    private static void initialize()
    {
        //Ensure the database path has already been set
        if(dbPath.isEmpty())
            throw new IllegalStateException("Cannot initialize database when database path is not set");

        //Create the databases, real or fake
        languageDB = new HSQLLanguageDB(dbPath, LANGUAGE_TABLE_NAME); //FakeDBFactory.createFakeLanguageDB();
        flashcardDB = new HSQLFlashcardDB(dbPath, FLASHCARD_TABLE_NAME, FLASHCARD_TAGS_TABLE_NAME); //FakeDBFactory.createFakeFlashcardDB();
        lessonDB = new HSQLLessonDB(dbPath, LESSON_TABLE_NAME, LESSON_MODULES_TABLE_NAME, LESSON_FLASHCARDS_TABLE_NAME); //FakeDBFactory.createFakeLessonDB();

        initialized = true;
    }
}