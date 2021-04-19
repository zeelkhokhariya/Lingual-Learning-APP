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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

//Tests the language and lesson selection lists by selecting a series of languages and lessons
@RunWith(AndroidJUnit4.class)
public class SelectionTest
{
    @Rule
    public ActivityScenarioRule<LanguageListActivity> mActivityTestRule = new ActivityScenarioRule<>(LanguageListActivity.class);

    @Test
    public void SelectionTest()
    {
        ResetDatabase.reset();

        //Select Japanese from the list
        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.lang_list_language_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(3);
        linearLayout.perform(click());

        //Ensure that Japanese was selected correctly
        ViewInteraction textView = onView(
                allOf(withText("Japanese"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView.check(matches(withText("Japanese")));

        //Click the learn button to go to the Japanese Lesson list
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.lor_button_learn), withText("Learn"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        //Click the first lesson in the list
        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        linearLayout2.perform(click());

        //Back out of the lesson
        pressBack();

        //Click the fourth lesson in the list
        DataInteraction linearLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(3);
        linearLayout3.perform(click());

        //Back out of the lesson
        pressBack();

        //Attempt to click the third item in the list, which is locked
        //This should not do anything
        DataInteraction linearLayout4 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(2);
        linearLayout4.perform(click());

        //Back out of the lesson list
        pressBack();

        //Back out of Japanese
        pressBack();

        //Select Hindu from the list
        DataInteraction linearLayout5 = onData(anything())
                .inAdapterView(allOf(withId(R.id.lang_list_language_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(2);
        linearLayout5.perform(click());

        //Back out of Hindu
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
