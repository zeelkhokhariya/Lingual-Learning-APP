package com.group5.lingual;

import com.group5.lingual.logic.FlashcardQueueIntegrationTest;
import com.group5.lingual.logic.LanguageListIntegrationTest;
import com.group5.lingual.logic.LessonListIntegrationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//Add all integration test classes into the class list below
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FlashcardQueueIntegrationTest.class,
        LanguageListIntegrationTest.class,
        LessonListIntegrationTest.class})
public class AllIntegrationTests
{}