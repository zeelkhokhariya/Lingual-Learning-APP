package com.group5.lingual.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//Creates a copy of the database for testing
//Acts somewhat like a test double for AssetLoader, which is unusable because it requires instrumentation
//However, it is not a true test double because they both use static methods, and it does not behave quite the same
//Call cleanup to clean up the data after testing is complete
public class DBLoader
{
    static final String SOURCE_PATH = "src/test/assets/db";
    static final String TARGET_PATH = "src/test/data/db";

    //Creates a copy, returns the new path
    public static String loadDB()
    {
        try
        {
            Files.createDirectories(Paths.get(TARGET_PATH));
            copyDirectory(new File(SOURCE_PATH), new File(TARGET_PATH));
        }
        catch(IOException e)
        {
            throw new IllegalStateException("Database not loaded:", e);
        }

        return TARGET_PATH;
    }

    //Deletes the copy that was created, and all associated files
    public static void cleanup()
    {
        deleteDirectory(new File(TARGET_PATH));
    }

    //Copies directory and its contents recursively
    private static void copyDirectory(File source, File target) throws IOException
    {
        if(!target.exists())
            target.mkdir();

        if(source.isDirectory())
        {
            String[] children = source.list();
            for(String s :children)
                copyDirectory(new File(source, s), new File(target, s));
        }
        else
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    //Deletes directory and its contents recursively
    private static void deleteDirectory(File file)
    {
        if(file.isDirectory())
        {
            File[] children = file.listFiles();
            for(File f : children)
                deleteDirectory(f);
        }

        file.delete();
    }
}
