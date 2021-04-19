package com.group5.lingual.dso.lessons;

import java.util.List;

//An interface for the details of a lesson available in the app
public interface ILesson extends ILessonSummary
{
    //Getter for an ordered list of the modules containing the lesson's content
    public List<ILessonModule> getModules();

    //Getter for a collection of flashcard IDs to be unlocked when the lesson is completed
    public List<Integer> getTiedFlashcards();
}