What technical debt has been cleaned up
========================================

Show links to a commit where you paid off technical debt. Write 2-5 sentences
that explain what debt was paid, and what its classification is.


[Link to violation found by Rob](https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/issues/101)

[Link to technical debt paid off](https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/blob/master/app/src/main/java/com/group5/lingual/logic/FlashcardFactory.java#L57)


The debt was that we had an open close violation when we created a flashcard from the HSQL database. We paid off the technical debt by creating a method within Factory that would register all the flashcard types.


What technical debt did you leave?
==================================

What one item would you like to fix, and can't? Anything you write will not
be marked negatively. Classify this debt.


An item our group would like to fix but can not is we have an open close violation that occurs when creating a new flashcard type or lesson module. We have found and spoken with Rob that this open close violation is practically unavoidable. We did fix some of our code but we could not entirely get rid of the violation occurring because the class must be registered.

[Link to technical debt left](https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/blob/master/app/src/main/java/com/group5/lingual/presentation/lessonfragments/LessonFragmentFactory.java#L78)



Discuss a Feature or User Story that was cut/re-prioritized
============================================

When did you change the priority of a Feature or User Story? Why was it
re-prioritized? Provide a link to the Feature or User Story. This can be from any
iteration.


A feature that we cut from being complete in iteration 3 was to reward the user once they finished a new lesson. It was re-prioritized because we under-estimated how much time and work it would take to complete some of our high priority features. Since we had to take longer on implementing the high priority features it pushed back rewarding a user after they complete a lesson because it was low priority. Our group thought it would be a fun addition to do if we were to have free time, but we do not.

[Link to feature](https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/issues/91)


Acceptance test/end-to-end
==========================

Write a discussion about an end-to-end test that you wrote. What did you test,
how did you set up the test so it was not flaky? Provide a link to that test.


An end-to-end test we wrote was to check to see if our flashcard review system worked correctly by allowing the user to review flashcards. We wrote this test by setting up a path that would first choose French from the language lesson test. Once French was chosen the test will then click on the Review button. A flashcard will then be displayed and the test will then check to see that the flashcards work by giving the right and wrong answer. It will first click the Reveal button and select the right answer where then a new flashcard will be displayed. It will then click the Reveal button for the new flashcard and click the Wrong button to answer the flashcard incorrectly. The test will then continue with two more flashcards of the same process of clicking the Reveal button and for one flashcard clicking the Right button and the other flashcard clicking the Wrong button. Our test will then back out of flashcard review and back out of French as the language chosen.  

We created the test so it was not flaky by adding dependencies so that the functionality of our app could be tested without failures. A common error we faced when first writing our acceptance tests was a timeout error. This error was occurring because our database was taking too long to load the flashcards after a lesson completed. We fixed this problem by improving our database which then helped to make the tests more reliable by eliminating that error from happening.

[Link to test](https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/blob/master/app/src/androidTest/java/com/group5/lingual/presentation/FlashcardReviewTest.java#L36)


Acceptance test, untestable
===============

What challenges did you face when creating acceptance tests? What was difficult
or impossible to test?

A challenge that we ran into when creating acceptance tests was that we kept getting errors and our tests would fail because of the button placement. We figured out that it was a hierarchy error and we fixed this by changing our constraint layout refer to the ID of a button instead of the position of a button that a linear layout may require. Another challenge that we faced was that we have to reinstall our app after an acceptance test. How we have our app set up is so that once a button is pressed for example to learn a lesson, you cannot re-click the button to learn the lesson again. So when going from one test to the next test the button would not work unless the app was reinstalled. For the time being this is something impossible that we can not change unless we were to continue developing our application. 


Velocity/teamwork
=================

Did your estimates get better or worse through the course? Show some
evidence of the estimates/actuals from tasks.


Our estimates got better throughout the course. As you can see in our velocity graph that is linked below, although from iteration one to iteration two we took on more hours of work because some of iteration one features were moved to iteration two. In iteration two we were able to complete more tasks and have a higher actual time for iteration two than iteration one. In our final iteration, iteration three, we were able to complete more hours than we originally committed to add additional features to our app.

![Velocity Graph](retrospective_velocity_graph.png)