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
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

//Tests the process of unlocking lessons by completing their prerequisite lessons
@RunWith(AndroidJUnit4.class)
public class LessonUnlockTest
{
    @Rule
    public ActivityScenarioRule<LanguageListActivity> mActivityTestRule = new ActivityScenarioRule<>(LanguageListActivity.class);

    @Test
    public void LessonUnlockTest()
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

        //Click Learn to go to the lesson list
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.lor_button_learn), withText("Learn"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        //Attempt to select the third lesson in the list
        //It is locked, so nothing should happen
        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(2);
        linearLayout2.perform(click());

        //Select the second lesson in the list
        //It is the prerequisite for the fifth lesson
        DataInteraction linearLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(1);
        linearLayout3.perform(click());

        //Complete the lesson, bringing us back to the lesson list
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.lesson_button_complete), withText("Complete Lesson"),
                        childAtPosition(
                                allOf(withId(R.id.lesson_layout_container),
                                        childAtPosition(
                                                withId(R.id.lesson_scroll_container),
                                                0)),
                                1)));
        materialButton2.perform(scrollTo(), click());

        //Select the fifth lesson, which should now be unlocked
        DataInteraction linearLayout4 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(4);
        linearLayout4.perform(click());

        //Back out all the way to the language list
        pressBack();
        pressBack();
        pressBack();

        //Select French
        DataInteraction linearLayout5 = onData(anything())
                .inAdapterView(allOf(withId(R.id.lang_list_language_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        linearLayout5.perform(click());

        //Click Learn to go to the lesson list
        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.lor_button_learn), withText("Learn"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        //Select the first lesson in the list, which is the prerequisite for the third lesson
        DataInteraction linearLayout6 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        linearLayout6.perform(click());

        //Complete the lesson
        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.lesson_button_complete), withText("Complete Lesson"),
                        childAtPosition(
                                allOf(withId(R.id.lesson_layout_container),
                                        childAtPosition(
                                                withId(R.id.lesson_scroll_container),
                                                0)),
                                1)));
        materialButton4.perform(scrollTo(), click());

        //Select the third item in the list, which should now be unlocked
        DataInteraction linearLayout7 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(2);
        linearLayout7.perform(click());

        //Back out of the lesson
        pressBack();

        //Back out of the lesson list
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
