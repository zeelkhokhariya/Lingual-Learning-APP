package com.group5.lingual.data.fakedb;

import com.group5.lingual.dso.lessons.ILesson;
import com.group5.lingual.dso.lessons.Lesson;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FakeLessonDBTest
{
    FakeLessonDB db = new FakeLessonDB();

    //Normal
    ILesson l1 = new Lesson(1, 2,"Lesson1", 4, "ic_lesson_basic", -1, Collections.emptyList());
    ILesson l2 = new Lesson(2, 2,"Lesson2", 5, "ic_lesson_basic", -1, Collections.emptyList());

    //Added to the DB, different language
    ILesson l3 = new Lesson(3, 3,"Lesson3", 6, "ic_lesson_basic", -1, Collections.emptyList());

    //Not added to the DB
    ILesson l4 = new Lesson(4, 2,"Lesson4", 7, "ic_lesson_basic", -1, Collections.emptyList());

    @Before
    public void setUp() throws Exception
    {
        db.addLesson(l1, false);
        db.addLesson(l2, false);
        db.addLesson(l3, false);
    }

    @Test
    public void getLesson()
    {
        assertEquals(l1, db.getLesson(1));
        assertEquals(l2, db.getLesson(2));
        assertEquals(l3, db.getLesson(3));
        assertThrows(IllegalArgumentException.class, () -> db.getLesson(4));
    }

    @Test
    public void getAllIDs()
    {
        List<Integer> idList = db.getAllIDs(2);
        assertTrue(idList.contains(1));
        assertTrue(idList.contains(2));
        assertFalse(idList.contains(3));
        assertFalse(idList.contains(4));
        assertEquals(2, idList.size());
    }

    @Test
    public void lessonsCount()
    {
        assertEquals(2, db.lessonCount(2));
        assertEquals(1, db.lessonCount(3));
        db.addLesson(l4, false);
        assertEquals(3, db.lessonCount(2));
    }

    @Test
    public void addDuplicateLesson()
    {
        assertThrows(IllegalArgumentException.class, () -> db.addLesson(l1, false));
        assertThrows(IllegalArgumentException.class, () -> db.addLesson(new Lesson(2, 3, "Lesson2Alt", 10, "ic_lesson_basic", -1, Collections.emptyList()), false));
    }

    @Test
    public void removeLesson()
    {
        //Before removal
        assertEquals(2, db.lessonCount(2));
        assertEquals(l1, db.getLesson(1));
        assertEquals(2, db.getAllIDs(2).size());

        db.removeLesson(l1);

        //After removal
        assertEquals(1, db.lessonCount(2));
        assertThrows(IllegalArgumentException.class, () -> db.getLesson(1));
        assertEquals(l2, db.getLesson(2));
        assertEquals(1, db.getAllIDs(2).size());

        //Removing a nonexistent language should throw an exception
        assertThrows(IllegalArgumentException.class, () -> db.removeLesson(l1));
        assertThrows(IllegalArgumentException.class, () -> db.removeLesson(l4));
    }
}