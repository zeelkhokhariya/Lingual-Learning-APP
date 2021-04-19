### Lingual Branching Strategy - GitHub Flow

For our application, we used a version of the [GitHub Flow](http://scottchacon.com/2011/08/31/github-flow.html) branching strategy detailed by Scott Chacon. This strategy, and our take on it, involves a number of rules/guidelines to follow.

**1. The main branch**

The main branch should always remain stable, and we should always be able to pull from it. This is maintained by the practice that something should only be merged into the main branch once it is considered 'release ready', meaning that features are only merged back into the main branch after they meet the requirements for that feature, and have been heavily tested. It is very rare to directly edit the project on the main branch as we don't want to risk putting it in an unstable state.

**2. Branching**

From the main branch, numerous descriptive branches are made for each issue in our project such as for every user story or every development task. These named branches get pushed to constantly as small changes and additions occur.

**3. Committing**

As mentioned, we push and commit changes to our branches regularly. This acceptable to do because we won't mess up anyone else's work since everything that is not in the main branch is considered as being worked on/in development.

**4. Merging**

When the assignee of a development task feels that their code fully implements the requirements of that task and has been heavily tested, a merge request for the branch can be made. Then, the other developers of the project can review the request and report back any issues they may have/find to the assignee of the task. Once all these issues are fixed/agreed upon, the branch can safely be merged back into the main branch.

**Advantages:**

Using this strategy provides our team with numerous advantages in regards to our workflow.

Firstly, the main branch is always kept in a stable state meaning that there is always something safe to pull from and create new branches off of. If there is an issue in the main branch, commits will be reverted or new commits will be made to fix the issue.

In addition, having lots of descriptive branches breaks things down and keeps things organized. The many branches in the project helps us as developers stay organized and on task for what we are currently supposed to be implementing for the project and allows for multiple people to work on the same feature at one time.

Furthermore, by committing changes constantly to our branches, these changes often end up being small which makes it easier to revert back to previous states and isolate where in the code a bug might be. This system is also motivational! Making lots of small changes and seeing all of those commits reflected in the change log provides the feeling that you're constantly making progress.

As well, reviewing each other's code by making merge requests ensures that there is always at least 2 people who have looked at the code which allows any issues and bugs to be found faster and more frequently.

And finally, the rules are few and easy to remember. A simple system means less mental overhead and stress on the team which is always a welcome benefit.
