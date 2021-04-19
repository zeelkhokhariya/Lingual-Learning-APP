package com.group5.lingual.presentation;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

//Navigates the menu in the first activity of the app to reset the database to its default state
public class ResetDatabase
{
    //Performs the reset
    public static void reset()
    {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Reset database")).perform(click());
        onView(withText(android.R.string.yes)).perform(click());
    }
}
