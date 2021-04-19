package com.group5.lingual.presentation.lessonfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.group5.lingual.dso.lessons.ILessonModule;
import com.group5.lingual.dso.lessons.ItemsListLessonModule;
import com.group5.lingual.dso.lessons.ParagraphLessonModule;
import com.group5.lingual.dso.lessons.QuizLessonModule;

import java.util.HashMap;
import java.util.Map;

//Creates UI fragments to display the content stored in lesson modules
public class LessonFragmentFactory
{
    private static final String MODULE_ARGUMENT_LABEL = "module"; //Label of the module within the fragment arguments
    private static final String ACTIVE_LANGUAGE_ARGUMENT_LABEL = "activeLanguageID"; //Label of the active language ID within the fragment arguments

    private static boolean initialRegistrations = false; //Whether the factory has performed its initial module type registrations

    //List of registrations mapping module types to fragment types
    private static Map<Class<? extends ILessonModule>, Class<? extends Fragment>> moduleTypes = new HashMap<Class<? extends ILessonModule>, Class<? extends Fragment>>();

    public static Fragment createLessonFragment(ILessonModule module, int activeLanguageID)
    {
        if(!initialRegistrations) //Ensure the initial module type registrations have already been performed
            initializeRegistrations();

        Fragment result; //The resulting fragment

        //Find the lowest-level module type that is registered with this factory
        Class moduleType = module.getClass();
        while(!moduleTypes.containsKey(moduleType))
        {
            moduleType = moduleType.getSuperclass(); //If no registration for this class, try its superclass
            if (!ILessonModule.class.isAssignableFrom(moduleType)) //If we've gone too far up the class hierarchy, then no registration was found
                throw new IllegalArgumentException("Module type " + module.getClass().getName() + " is not registered to a fragment type");
        }

        //Get the corresponding fragment type
        Class<? extends Fragment> fragmentType = moduleTypes.get(moduleType);

        //Create the fragment
        try
        {
            result = fragmentType.newInstance();
        }
        catch(IllegalAccessException | InstantiationException e)
        {
            throw new IllegalArgumentException("Failed to instantiate lesson fragment", e);
        }

        //Add the module as an argument for the fragment
        Bundle args = new Bundle();
        args.putSerializable(MODULE_ARGUMENT_LABEL, module);
        args.putInt(ACTIVE_LANGUAGE_ARGUMENT_LABEL, activeLanguageID);
        result.setArguments(args);

        return result;
    }

    //Getters for the fragment argument list labels
    public static String getModuleArgumentLabel()
    {
        return MODULE_ARGUMENT_LABEL;
    }
    public static String getActiveLanguageArgumentLabel() { return ACTIVE_LANGUAGE_ARGUMENT_LABEL; }

    //Add a module type to the registration list for future instantiation
    public static void registerModuleType(Class<? extends ILessonModule> moduleType, Class<? extends Fragment> fragmentType)
    {
        moduleTypes.put(moduleType, fragmentType);
    }

    //Register all of the module types
    private static void initializeRegistrations()
    {
        registerModuleType(ParagraphLessonModule.class, ParagraphLessonFragment.class);
        registerModuleType(QuizLessonModule.class, QuizLessonFragment.class);
        registerModuleType(ItemsListLessonModule.class, ItemsListLessonFragment.class);
        initialRegistrations = true;
    }
}
