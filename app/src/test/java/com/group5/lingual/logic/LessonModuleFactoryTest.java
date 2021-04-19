package com.group5.lingual.logic;

import com.group5.lingual.dso.lessons.ILessonModule;
import com.group5.lingual.dso.lessons.ProperLessonModuleFake;
import com.group5.lingual.dso.lessons.WrongLessonModuleFake;

import org.junit.Test;

import static org.junit.Assert.*;

public class LessonModuleFactoryTest
{
    //Register and create an instance of a class that implements ILessonModule and has the appropriate default constructor
    @Test
    public void createProperModule()
    {
        //Attempting to create an instance of a class that is not registered with the factory should result in an exception
        assertThrows(IllegalArgumentException.class, () -> LessonModuleFactory.createLessonModule("test data", "ProperLessonModuleFake"));

        //Register the module, create an instance, and ensure the instance is correct
        LessonModuleFactory.registerModuleType(ProperLessonModuleFake.class);
        ILessonModule module = LessonModuleFactory.createLessonModule("test data", "ProperLessonModuleFake");
        assertEquals("test data", module.getContentData());
    }

    //Attempt to register and create an instance of a class that implements ILessonModule, but lacks a default constructor
    @Test
    public void createWrongConstructorModule()
    {
        //Registering that module should succeed, but attempting to create an instance should result in an exception
        LessonModuleFactory.registerModuleType(WrongLessonModuleFake.class);
        assertThrows(IllegalArgumentException.class, () -> LessonModuleFactory.createLessonModule("test data", "WrongLessonModuleFake"));
    }
}