package com.group5.lingual.presentation;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group5.lingual.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

//Tests the item list lesson module, viewing a lesson that contains it and expanding different items in the list
@RunWith(AndroidJUnit4.class)
public class ItemListModuleTest
{
    @Rule
    public ActivityScenarioRule<LanguageListActivity> mActivityTestRule = new ActivityScenarioRule<>(LanguageListActivity.class);

    @Test
    public void ItemListModuleTest()
    {
        ResetDatabase.reset();

        //Select Japanese
        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.lang_list_language_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(3);
        linearLayout.perform(click());

        //Click Learn to go to the Lesson list
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.lor_button_learn), withText("Learn"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        //Select the fourth lesson, which contains an item list
        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(3);
        linearLayout2.perform(click());

        //Expand the first item in the first column
        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.lmod_ilist_layout_col1),
                                childAtPosition(
                                        withId(R.id.lmod_ilist_layout_items),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout3.perform(click());

        //Back out of the item
        pressBack();

        //Expand the first item in the second column
        ViewInteraction linearLayout4 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.lmod_ilist_layout_col2),
                                childAtPosition(
                                        withId(R.id.lmod_ilist_layout_items),
                                        1)),
                        0),
                        isDisplayed()));
        linearLayout4.perform(click());

        //Back out of the item
        pressBack();

        //Expand the second item in the second column
        ViewInteraction linearLayout5 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.lmod_ilist_layout_col2),
                                childAtPosition(
                                        withId(R.id.lmod_ilist_layout_items),
                                        1)),
                        1),
                        isDisplayed()));
        linearLayout5.perform(click());

        //Back out of the item
        pressBack();
    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view)
            {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
