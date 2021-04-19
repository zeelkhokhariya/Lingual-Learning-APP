package com.group5.lingual.logic;

import com.group5.lingual.data.ILessonDB;
import com.group5.lingual.data.fakedb.FakeLessonDB;
import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.ILessonModule;
import com.group5.lingual.dso.lessons.Lesson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LessonListTest
{
    ILessonDB db;
    IFlashcardQueue flashcardQueue;
    ILessonList list;

    //LESSON SAMPLES
    //Normal, unlocked from the start
    ILesson l1 = new Lesson(1, 2,"Lesson1", 4, "ic_lesson_basic", -1, Collections.emptyList());
    ILesson l2 = new Lesson(2, 2,"Lesson2", 5, "ic_lesson_basic", -1, Collections.emptyList());
    //Locked until l2 is completed
    ILesson l3 = new Lesson(3, 2, "Lesson3", 6, "ic_lesson_basic", 1, Collections.emptyList());
    //Added to the DB, different language
    ILesson l4 = new Lesson(4, 3,"Lesson4", 7, "ic_lesson_basic", -1, Collections.emptyList());
    //Not added to the DB
    ILesson l5 = new Lesson(5, 2,"Lesson5", 8, "ic_lesson_basic", -1, Collections.emptyList());

    @Before
    public void setUp() throws Exception
    {
        db = new FakeLessonDB();
        flashcardQueue = mock(IFlashcardQueue.class);

        list = new LessonList(2, db, flashcardQueue);

        db.addLesson(l1, false);
        db.addLesson(l2, false);
        db.addLesson(l3, false);
        db.addLesson(l4, false);
    }

    @Test
    public void getLesson()
    {
        //Test lesson retrieval
        assertEquals(l1, list.getLesson(1));
        assertEquals(l2, list.getLesson(2));

        //Locked lessons should be retrieved
        assertEquals(l3, list.getLesson(3));

        //Lessons from another language should not be retrieved
        assertThrows(IllegalArgumentException.class, () -> list.getLesson(4));

        //Lessons not in the database should not be retrieved
        assertThrows(IllegalArgumentException.class, () -> list.getLesson(5));

        //Test lesson summary retrieval as well
        assertEquals(l1, list.getLessonSummary(1));
        assertEquals(l2, list.getLessonSummary(2));
        assertThrows(IllegalArgumentException.class, () -> list.getLessonSummary(4));
        assertThrows(IllegalArgumentException.class, () -> list.getLessonSummary(5));
    }

    @Test
    public void getAllIDs()
    {
        //Ensure getAllIDs includes ID if and only if the lesson is in this language and in the DB
        List<Integer> idList = list.getAllIDs();
        assertTrue(idList.contains(1));
        assertTrue(idList.contains(2));
        assertTrue(idList.contains(3));
        assertFalse(idList.contains(4));
        assertFalse(idList.contains(5));
        assertEquals(3, idList.size());
    }

    @Test
    public void lessonCounts()
    {
        //Ensure lessonCount counts l1, l2, and l3, while unlockedLessonCount only counts l1 and l2
        assertEquals(3, list.lessonCount());
        assertEquals(2, list.unlockedLessonCount());

        //Adding another unlocked lesson should increase both counts
        db.addLesson(l5, false);
        assertEquals(4, list.lessonCount());
        assertEquals(3, list.unlockedLessonCount());

        //Completing lesson l1, which is l3's prerequisite, should unlock l3 and increase the unlocked count
        list.completeLesson(l1);
        assertEquals(4, list.lessonCount());
        assertEquals(4, list.unlockedLessonCount());
    }

    @Test
    public void durations()
    {
        //No lessons are completed, so both total and remaining should include all lessons
        //for this language that are in the database, whether locked or unlocked
        int expected = l1.getDuration() + l2.getDuration() + l3.getDuration();
        assertEquals(expected, list.totalDuration());
        assertEquals(expected, list.remainingDuration());

        //Adding a lesson to the database should increase the count
        db.addLesson(l5, false);
        expected += l5.getDuration();
        assertEquals(expected, list.totalDuration());

        //Completing a lesson should remove it from the remaining count, but keep it in total
        list.completeLesson(l1);
        assertEquals(expected, list.totalDuration());
        assertEquals(expected - l1.getDuration(), list.remainingDuration());
    }

    @Test
    public void lessonIsCompleted()
    {
        //An non-completed lesson should produce false
        assertFalse(list.lessonIsCompleted(l1));

        //After completing the lesson, it should produce true
        list.completeLesson(l1);
        assertTrue(list.lessonIsCompleted(l1));

        //Checking a lesson from a different language should result in an exception
        assertThrows(IllegalArgumentException.class, () -> list.lessonIsCompleted(l4));

        //Checking a lesson that isn't in the database should result in an exception
        assertThrows(IllegalArgumentException.class, () -> list.lessonIsCompleted(l5));
    }

    @Test
    public void completeLessonBasics()
    {
        //Ensure neither of these lessons are yet completed
        assertFalse(list.lessonIsCompleted(l1));
        assertFalse(list.lessonIsCompleted(l3));

        //Attempting to complete a locked lesson should result in an exception
        assertThrows(IllegalArgumentException.class, () -> list.completeLesson(l3));

        //Completing the prerequisite of the above lesson should resolve the issue
        list.completeLesson(l1);
        list.completeLesson(l3);

        //Both of the above lessons should now be marked as completed
        assertTrue(list.lessonIsCompleted(l1));
        assertTrue(list.lessonIsCompleted(l3));

        //Attempting to complete one of the above lessons again should result in an exception
        assertThrows(IllegalStateException.class, () -> list.completeLesson(l1));

        //Attempting to complete a lesson from a different language should result in an exception
        assertThrows(IllegalArgumentException.class, () -> list.completeLesson(l4));

        //Attempting to complete a lesson that isn't in the database should result in an exception
        assertThrows(IllegalArgumentException.class, () -> list.completeLesson(l5));
    }

    @Test
    public void completeLessonWithModule()
    {
        //Create a lesson, add a module to it, and add the lesson to the database
        Lesson lModuleTest = new Lesson(6, 2, "ModuleLesson", 10,
                "ic_lesson_basic", -1, Collections.emptyList());
        ILessonModule testModule = mock(ILessonModule.class);
        lModuleTest.addModule(testModule);
        db.addLesson(lModuleTest, false);

        //While the module is not completed, attempting to complete the lesson should result in an exception
        when(testModule.isCompleted()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> list.completeLesson(lModuleTest));

        //Once the module is completed, the lesson should be able to be be completed
        when(testModule.isCompleted()).thenReturn(true);
        list.completeLesson(lModuleTest);
    }

    @Test
    public void completeLessonWithFlashcards()
    {
        //Add a lesson that has flashcards associated with it
        Lesson lWithFlashcards = new Lesson(6, 2, "FlashcardLesson", 10,
                "ic_lesson_basic", -1, Arrays.asList(1, 2));
        db.addLesson(lWithFlashcards, false);

        //Complete that lesson
        list.completeLesson(lWithFlashcards);

        //Verify that completing the lesson resulted in attempts to unlock those flashcards
        verify(flashcardQueue).unlockCard(1);
        verify(flashcardQueue).unlockCard(2);
        verifyNoMoreInteractions(flashcardQueue);
    }

    @Test
    public void lessonIsLocked()
    {
        //A lesson with no prerequisite should produce false
        assertFalse(list.lessonIsLocked(l1));

        //A lesson with a non-completed prerequisite should produce true
        assertTrue(list.lessonIsLocked(l3));

        //After completing the prerequisite lesson, it should produce false
        list.completeLesson(l1);
        assertFalse(list.lessonIsLocked(l3));

        //Checking a lesson from a different language should result in an exception
        assertThrows(IllegalArgumentException.class, () -> list.lessonIsLocked(l4));
    }
}