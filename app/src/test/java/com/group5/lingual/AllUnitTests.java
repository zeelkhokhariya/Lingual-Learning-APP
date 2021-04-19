package com.group5.lingual;

import com.group5.lingual.data.fakedb.FakeLanguageDBTest;
import com.group5.lingual.data.fakedb.FakeFlashcardDBTest;
import com.group5.lingual.data.fakedb.FakeLessonDBTest;
import com.group5.lingual.dso.lessons.LessonTest;
import com.group5.lingual.dso.flashcards.SimpleFlashcardTest;
import com.group5.lingual.dso.languages.LanguageTest;
import com.group5.lingual.logic.FlashcardFactoryTest;
import com.group5.lingual.logic.FlashcardQueueTest;
import com.group5.lingual.logic.LanguageListTest;
import com.group5.lingual.logic.LessonListTest;
import com.group5.lingual.logic.LessonModuleFactoryTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//Add all unit test classes into the class list below
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FakeLanguageDBTest.class,
        FakeFlashcardDBTest.class,
        FakeLessonDBTest.class,
        SimpleFlashcardTest.class,
        LanguageTest.class,
        LessonTest.class,
        FlashcardQueueTest.class,
        LanguageListTest.class,
        LessonListTest.class,
        LessonModuleFactoryTest.class,
        FlashcardFactoryTest.class})
public class AllUnitTests
{}
