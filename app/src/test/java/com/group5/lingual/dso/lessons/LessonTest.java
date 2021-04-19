package com.group5.lingual.dso.lessons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class LessonTest
{
    @Test
    public void getID()
    {
        assertEquals(123, new Lesson(123, 2, "LessonA", 5, "ic_lesson_basic", -1, Collections.emptyList()).getID());
    }

    @Test
    public void getLanguageID()
    {
        assertEquals(2, new Lesson(123, 2, "LessonA", 5, "ic_lesson_basic", -1, Collections.emptyList()).getLanguageID());
    }

    @Test
    public void getName()
    {
        assertEquals("LessonA", new Lesson(123, 2, "LessonA", 5, "ic_lesson_basic", -1, Collections.emptyList()).getName());
    }

    @Test
    public void getDuration()
    {
        assertEquals(5, new Lesson(123, 2, "LessonA", 5, "ic_lesson_basic", -1, Collections.emptyList()).getDuration());
    }

    //Should throw IllegalArgumentException when trying to create a lesson with a non-positive lesson ID or language ID
    @Test
    public void invalidID()
    {
        assertThrows(IllegalArgumentException.class, () -> new Lesson(0, 1, "Lesson", 1, "ic_lesson_basic", -1, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> new Lesson(-1, 1, "Lesson", 1, "ic_lesson_basic", -1, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> new Lesson(1, 0, "Lesson", 1, "ic_lesson_basic", -1, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> new Lesson(2, -1, "Lesson", 1, "ic_lesson_basic", -1, Collections.emptyList()));
    }

    //Should throw IllegalArgumentException when trying to create a lesson with a non-positive duration
    @Test
    public void invalidDuration()
    {
        assertThrows(IllegalArgumentException.class, () -> new Lesson(1, 1, "Lesson", 0, "ic_lesson_basic", -1, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> new Lesson(2, 1, "Lesson", -1, "ic_lesson_basic", -1, Collections.emptyList()));
    }

    @Test
    public void equals()
    {
        //Test l against several objects, one equal and one different in each possible way
        Lesson l = new Lesson(1, 1, "Lesson", 1, "icon", -1, Arrays.asList(1, 2));
        assertEquals(l, new Lesson(1, 1, "Lesson", 1, "icon", -1, Arrays.asList(1, 2)));
        assertNotEquals(l, new Lesson(2, 1, "Lesson", 1, "icon", -1, Arrays.asList(1, 2)));
        assertNotEquals(l, new Lesson(1, 2, "Lesson", 1, "icon", -1, Arrays.asList(1, 2)));
        assertNotEquals(l, new Lesson(1, 1, "Messon", 1, "icon", -1, Arrays.asList(1, 2)));
        assertNotEquals(l, new Lesson(1, 1, "Lesson", 2, "icon", -1, Arrays.asList(1, 2)));
        assertNotEquals(l, new Lesson(1, 1, "Lesson", 1, "jcon", -1, Arrays.asList(1, 2)));
        assertNotEquals(l, new Lesson(1, 1, "Lesson", 1, "icon", 0, Arrays.asList(1, 2)));
        assertNotEquals(l, new Lesson(1, 1, "Lesson", 1, "icon", -1, Arrays.asList(1)));
        assertNotEquals(l, new Lesson(1, 1, "Lesson", 1, "icon", -1, Arrays.asList(1, 2, 3)));
        assertNotEquals(l, null);
        assertNotEquals(l, 1);

        //Test equality when modules are involved
        ILessonModule module = Mockito.mock(ILessonModule.class);
        l.addModule(module);
        Lesson other = new Lesson(1, 1, "Lesson", 1, "icon", -1, Arrays.asList(1, 2));
        assertNotEquals(l, other);
        other.addModule(module);
        assertEquals(l, other);
    }
}