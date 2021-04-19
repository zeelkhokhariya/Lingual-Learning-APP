package com.group5.lingual;

import com.group5.lingual.data.AssetLoaderTest;
import com.group5.lingual.presentation.SelectionTest;
import com.group5.lingual.presentation.FlashcardReviewTest;
import com.group5.lingual.presentation.FlashcardUnlockTest;
import com.group5.lingual.presentation.LessonUnlockTest;
import com.group5.lingual.presentation.DictionaryLookupTest;
import com.group5.lingual.presentation.ItemListModuleTest;
import com.group5.lingual.presentation.QuizModuleTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//Add all system test classes into the class list below
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AssetLoaderTest.class,
        SelectionTest.class,
        FlashcardReviewTest.class,
        FlashcardUnlockTest.class,
        LessonUnlockTest.class,
        DictionaryLookupTest.class,
        ItemListModuleTest.class,
        QuizModuleTest.class})
public class AllSystemTests
{
}
