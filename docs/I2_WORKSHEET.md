Iteration 2 Worksheet
=====================

Paying off Technical Debt
-----------------

Show two instances of your group paying off technical debt. For these two instances:

Explain how your are paying off the technical debt.
Show commits, links to lines in your commit where you paid off technical debt.
Classify the debt, and justify why you chose that classification with 1-3 sentences.
Example of how to link to a diff - click on he commit in the commit log, then click on the margin to the line: https://code.cs.umanitoba.ca/3350-winter-2021-a01/sample-project/-/commit/8e38ae9c3084d62adc4ac5fafa3b87d7d862dc26#72899361f89777662df76c5ce0ed847af04dff86_35_41



1. Our first instance of technical debt. is how we implmeneted the flashcard types as a class hierarchy. With the way we implemented the flashcard types it worked well with the fake database but now that we are creating the real database it has made things complicated. We are paying it off by having to create a switch statement to handle the different types of flashcards.

Link to lines in commit where we paid off technical debt.:https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/blob/master/app/src/main/java/com/group5/lingual/data/hsqldb/HSQLFlashcardDB.java#L280


We would classify this technical debt. as Prudent Deliberate. We think it is Prudent Deliberate because when creating the flashcard types as a class hierarchy we were trying to implement it quickly to meet the iteration one deadline and were aware that by the way we were implementing it that it may cause issues in the future. 



2. Our second instance of technical debt. is for our flashcards we added search tags to the flashcards directly. The same problem as the above instance of technical debt. occured where for the fake database how we implemented the search tags to flashcards directly worked but now that the real database is being created we now see how we should have implemented it to have the database have it as a feature to store to the flashcard. We are paying off technical debt. by creating a tagsTable in the database to adapt betwen the set and relational database.

Link to lines in commit where we paid off technical debt.: https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/blob/master/app/src/main/java/com/group5/lingual/data/hsqldb/HSQLFlashcardDB.java#L300


We think it is Prudent Inadvertant technical debt. because while implementing how we wanted the feature of having search tags for the flashcards it made sense to have it with the flashcards directly. Now that we have created a real database we now see that we should have added the search tags as a feature of the database the flashcard is stored in. We classify this debt. as Prudent Inadvertant because at the time of implementing the search tags we though how we did it was the best way and now that have advanced in our project we now see that we could have designed it differently.


SOLID
----------------

Find a SOLID violation in the project of group with the same group number in the next class over - (n%3)+1 (A03 does A01) Open an issue in their project with the violation, clearly explaining the SOLID violation - specifying the type, provide a link to that issue. Be sure your links in the issues are to specific commits (not to main, or develop as those will be changed).

Provide a link to the issue you created here.


A02 - Group 5 SOLID Violation, Dependency Inversion.

Link to issue created: https://code.cs.umanitoba.ca/3350-winter-2021-a02/group5/-/issues/85


Retrospective
----------

Describe how the retrospective has changed the way you are doing your project. Is there evidence of the change in estimating/committing/peer review/timelines/testing? Provide those links and evidence here - or explain why there is not evidence.


The retrospective has changed how we are assigning dev tasks. After iteration 1 we struggled to complete all the issues we wanted to because of the way we assigned tasks. The tasks were very dependent on waiting on another team memeber to finish their task before the next team memeber could even start their task. For iteration 2 we tried to assign tasks that worked well with one another and were not dependent on another team member to finish their work. We obvisouly could not eliminate that completely but we also tried to be mindful of who was taking the tasks 

Link to google doc were tasks are assigned: https://docs.google.com/document/d/1oBvmFJttx6bYeREmteaOUlMYiHNE-5qVC8FgyQrKZug/edit?usp=sharing


Design Patterns
-----

Show links to your project where you use a well-known design pattern. Which pattern is it? Provide links to the design pattern that you used.

Note: Though Dependency Injection is a programming pattern, we would like to see a programming pattern other than Dependency Injections.


A well-known design pattern we used is Singleton.

Link to singleton design pattern being used: https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/blob/master/app/src/main/java/com/group5/lingual/data/DBManager.java#L33


Iteration 1 Feedback Fixes
--------------

Provide a link to an issue opened by the grader.

Explain what the issue was, and why it was flagged. Explain what you did to refactor or fix your code to address the issue. Provide links to the commits where you fixed the issue.


Link to issue made by grader: 
https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/issues/64


The issue was that we would return null instead throwing an exception. It was flagged because by just returning null it does not terminate the methods where it should, so we should throw an exception. We fixed it by doing the suggestion and instead of returning null we throw an exception. We also added the keyword private to everything in the app that was missed in iteration one that should have had it.


Link to commit to fix the issue: https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/merge_requests/24



