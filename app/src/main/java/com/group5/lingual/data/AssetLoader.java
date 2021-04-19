package com.group5.lingual.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

//Manages the loading of the database and other assets onto the device
public class AssetLoader
{
    private static final String DB_PATH = "db";

    //If overwrite is true, any existing database in the data folder will be deleted and replaced with a fresh copy from assets
    public static void copyDatabaseToDevice(Context context, boolean overwrite) throws IOException
    {
        Log.d("Asset Loader", "Copying database, overwrite = " + overwrite);

        AssetManager assetManager = context.getAssets();

        String[] assetNames;
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);

        if(overwrite)
            deleteDirectory(dataDirectory);
        
        if(!dataDirectory.exists())
            dataDirectory.mkdir();

        assetNames = assetManager.list(DB_PATH);
        for(int i = 0; i < assetNames.length; i++)
            assetNames[i] = DB_PATH + "/" + assetNames[i];

        copyAssetsToDirectory(assetManager, assetNames, dataDirectory);

        DBManager.setDBPathName(dataDirectory.toString());
    }

    //Copies select assets to a given directory
    private static void copyAssetsToDirectory(AssetManager assetManager, String[] assetNames, File destination) throws IOException
    {
        for(String asset : assetNames)
        {
            String[] components = asset.split("/");
            String copyPath = destination.toString() + "/"  + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if(!outFile.exists())
            {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while(count != -1)
                {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
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