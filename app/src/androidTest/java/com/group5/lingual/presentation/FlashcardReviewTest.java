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

//Tests the flashcard review system by viewing and answering a few flashcards
@RunWith(AndroidJUnit4.class)
public class FlashcardReviewTest
{
    @Rule
    public ActivityScenarioRule<LanguageListActivity> mActivityTestRule = new ActivityScenarioRule<>(LanguageListActivity.class);

    @Test
    public void FlashcardReviewTest()
    {
        ResetDatabase.reset();

        //Select French from the list
        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.lang_list_language_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(click());

        //Click the Review button to start reviewing flashcards
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.lor_button_review), withText("Review"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        //Click the Reveal button to reveal the card's answer
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.flash_button_reveal), withText("Reveal"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.flash_layout_root),
                                        1),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        //Click the Right button to answer the card correctly
        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.flash_button_right), withText("Right"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        //Click the Reveal button to reveal another card's answer
        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.flash_button_reveal), withText("Reveal"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.flash_layout_root),
                                        1),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());

        //Click the Wrong button to answer this card wrong
        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.flash_button_wrong), withText("Wrong"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        materialButton5.perform(click());

        //Repeat the above pattern with two more cards
        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.flash_button_reveal), withText("Reveal"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.flash_layout_root),
                                        1),
                                2),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.flash_button_right), withText("Right"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.flash_button_reveal), withText("Reveal"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.flash_layout_root),
                                        1),
                                2),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.flash_button_wrong), withText("Wrong"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        materialButton9.perform(click());

        //Back out of flashcard reviewing
        pressBack();

        //Back out of French
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
