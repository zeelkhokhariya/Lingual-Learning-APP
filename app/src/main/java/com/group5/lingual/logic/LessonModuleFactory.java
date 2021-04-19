package com.group5.lingual.logic;

import com.group5.lingual.dso.lessons.ILessonModule;
import com.group5.lingual.dso.lessons.ItemsListLessonModule;
import com.group5.lingual.dso.lessons.ParagraphLessonModule;
import com.group5.lingual.dso.lessons.QuizLessonModule;

import java.util.HashMap;
import java.util.Map;

//Creates lesson modules from their encoding in the database
public class LessonModuleFactory
{
    private static boolean initialRegistrations = false; //Whether the factory has performed its initial module type registrations

    private static Map<String, Class<? extends ILessonModule>> moduleTypes = new HashMap<String, Class<? extends ILessonModule>>(); //List of registered module types

    //Given its encoded contentData and the name of the type, create an instance of the desired module type
    public static ILessonModule createLessonModule(String contentData, String typeName)
    {
        if(!initialRegistrations) //Ensure the initial module type registrations have already been performed
            initializeRegistrations();

        ILessonModule m; //The created module

        //Get the type of the desired module
        Class<? extends ILessonModule> moduleType = moduleTypes.get(typeName);
        if(moduleType == null) //Check that the type was retrieved successfully
            throw new IllegalArgumentException("Module type name " + typeName + " not registered");

        //Instantiate a module of that type
        try
        {
            m = moduleType.newInstance();
        }
        catch(IllegalAccessException | InstantiationException e)
        {
            throw new IllegalArgumentException("Failed to instantiate lesson module", e);
        }

        m.initializeContent(contentData);

        return m;
    }

    //Add a module type to the list for future instantiation
    public static void registerModuleType(Class<? extends ILessonModule> type)
    {
        //Get the name of the type
        String typeName = type.getName();
        typeName = typeName.substring(typeName.lastIndexOf('.') + 1);

        moduleTypes.put(typeName, type);
    }

    //Register all of the module types
    private static void initializeRegistrations()
    {
        registerModuleType(ParagraphLessonModule.class);
        registerModuleType(QuizLessonModule.class);
        registerModuleType(ItemsListLessonModule.class);
        initialRegistrations = true;
    }
}
