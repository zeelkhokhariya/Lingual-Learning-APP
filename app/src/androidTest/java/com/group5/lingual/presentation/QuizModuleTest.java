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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

//Tests the quiz lesson module by unlocking a lesson with a quiz, and then completing that lesson
@RunWith(AndroidJUnit4.class)
public class QuizModuleTest
{
    @Rule
    public ActivityScenarioRule<LanguageListActivity> mActivityTestRule = new ActivityScenarioRule<>(LanguageListActivity.class);

    @Test
    public void QuizModuleTest()
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

        //Select the first lesson, which is the prerequisite for the lesson with the quiz
        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        linearLayout2.perform(click());

        //Complete the lesson
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.lesson_button_complete), withText("Complete Lesson"),
                        childAtPosition(
                                allOf(withId(R.id.lesson_layout_container),
                                        childAtPosition(
                                                withId(R.id.lesson_scroll_container),
                                                0)),
                                1)));
        materialButton2.perform(scrollTo(), click());

        //Select the third lesson, which has a quiz
        DataInteraction linearLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(2);
        linearLayout3.perform(click());

        //Answer quiz questions until they are all exhausted,
        //alternating between clicking reveal and next
        ViewInteraction materialButton3 = onView(withId(R.id.lmod_quiz_button_reveal));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialButton4 = onView(withId(R.id.lmod_quiz_button_next));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction materialButton5 = onView(withId(R.id.lmod_quiz_button_reveal));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(withId(R.id.lmod_quiz_button_next));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction materialButton7 = onView(withId(R.id.lmod_quiz_button_reveal));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction materialButton8 = onView(withId(R.id.lmod_quiz_button_next));
        materialButton8.perform(scrollTo(), click());

        ViewInteraction materialButton9 = onView(withId(R.id.lmod_quiz_button_reveal));
        materialButton9.perform(scrollTo(), click());

        ViewInteraction materialButton10 = onView(withId(R.id.lmod_quiz_button_next));
        materialButton10.perform(scrollTo(), click());

        //With all of the questions exhausted, complete the lesson
        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.lesson_button_complete), withText("Complete Lesson"),
                        childAtPosition(
                                allOf(withId(R.id.lesson_layout_container),
                                        childAtPosition(
                                                withId(R.id.lesson_scroll_container),
                                                0)),
                                1)));
        materialButton11.perform(scrollTo(), click());
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
