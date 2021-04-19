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
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

//Tests the mechanism for looking up words in online dictionaries
@RunWith(AndroidJUnit4.class)
public class DictionaryLookupTest
{
    @Rule
    public ActivityScenarioRule<LanguageListActivity> mActivityTestRule = new ActivityScenarioRule<>(LanguageListActivity.class);

    @Test
    public void DictionaryLookupTest() {
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

        //Select the first lesson
        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.less_list_lesson_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        linearLayout2.perform(click());

        //Attempt to perform a lookup when no text is selected
        //Nothing should happen
        lookupWord();

        //Select text and attempt a lookup
        ViewInteraction materialTextView1 = onView(
                allOf(withId(R.id.lmod_para_text_heading), withText("The は (wa) Particle")));
        materialTextView1.perform(scrollTo(), longClick());

        lookupWord();

        //Back out of the dictionary
        pressBack();

        //Select different text and attempt a lookup
        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.lmod_para_text_heading), withText("Introduction")));
        materialTextView2.perform(scrollTo(), longClick());

        lookupWord();

        //Back out to the language list
        pressBack();
        pressBack();
        pressBack();
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

    //Select the menu item to attempt to look up selected text
    private void lookupWord()
    {
        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.title), withText("Search selected text in dictionary"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());
    }
}
