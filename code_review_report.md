# SOLID Code Review Report 

## Assignments 
As a part of the review process, we each took different sections(written by different members) of the code to review. 

|   Name      |   File Name - Bug Fixing/Refactoring   |
| :-------------: |:--------------------------| 
| Jinny | bug fixing: student.vm </br> refactoring(adding docstrings): Event, Group, Instructor, SimpleSurveyQuestion|
| Danny| Event class, Daos.java, student.vm |
|Josh |  Recommender.java|
|Vincent |  Response page (removal) |
|Jonathan| .vm files|
|Mitchell| Bug fixing: Filling out a survey 500 error, Refactor </br> Feature Adding: Added group related features on the backend to help refine our application after users are matched via adding the ability to join and leave groups </br> Refactor: MainPage.java|




## Design 
In general, we feel that our design overall is reasonably appropriate, though our code oftentimes does not correspond fully to a more ideal design such as the patterns we've learned in the course. Importantly, we made decisions to step outside of our preferred patterns consciously, and unfortunately largely due to constraints by the tool stack we chose. 

In particular, there were limitations with ORMLite that meant we could not always use the design we wanted to. For example, our "SimpleQuestion" class would ideally be an abstract class which other questions could inherit from to reduce duplicate code later down the line. However, ORMLite would not support this, and there is no way via inheritance to run a query over a base class and yield results for all subclasses (as is answered [here](https://stackoverflow.com/questions/10188414/does-ormlite-support-inheritance) in a post from the primary author of ORMLite). We could use a very large interface for questions, but we would likely end up violating the Interface Segregation Principle.

This became particularly relevant in the case of Instructors and Students, in that we had a particularly bad case of duplicate code. To resolve this, in iteration four we completed the majority of the refactoring so that  both Student and Instructor have a new foreign object ParticipantInfo. ParticipantInfo contains the data that is common to all participants. importantly, this solution was endorsed by ORMLite's primary author in the post linked above as a reasonable way around the library's limitations, though as we found it did not always result in neat code. 

When it comes to the frontend that opened the view to our work, it was not generally the most considered, usually only as much as we needed to show our capability. Around Iteration 4 is when we started to make it more user friendly. Even still, from a design standpoint, it was more function over form. Focusing the most on the most traversed pages, we refactored the vms to streamline our view. We also changed our look to be larger and easier to read, and give it a lighter color scheme with darker backings for things like the groups. We also tried to make the javascript present when needed, and properly space our files so that it is easier to follow. Using pure css, we were able to make quick formatting tools to try and keep a consistent look throughout most of our site, but there are still lingering remnants of our first trial run of html.

## Complexity 

- Much of the application is CRUD focused outside of the recommendation algorithm, so in general these portions of the app likely cannot get much more simple. However, there are definitely optimizations that could streamline the app depending on whether the app is planned to be expanded on further or whether it is better as a simple one–time use application.

- We do have areas of the code that are complex, especially those involving sections where we needed to model objects in a many to many relationship (ie multiple different groups in different events can have the same student in it). Because ORMlite's Foreign Collections do not support this case, we modeled much of this behavior manually, which at times resulted in a messier code base than we would have liked. For future projects we would consider moving away from ORMlite for this reason.

- There are decisions we made, from code within functions to helper methods we created in various classes that may seem overly complex, but were intended to help with future development. For example, our matchParticipant method is long, but it actually already is set up in such a way that if in the future we wanted to allow users to join with partially formed teams our recommendation algorithm could support this with only minor changes. This would be useful for cases where friends want to work together, or partial teams are looking for additional members.
- There are methods in the app that are larger, but generally those end up being Spark post and get methods which can become more confusing if they were to be split up.



## Tests 

We did not create automated tests, as we felt with how rapidly our iterations moved it was more efficient to perform intense and frequent manual testing. Notably, for this review, we developed a list of bugs we were able to find, which we then split up and addressed individually. The bugs we each fixed are detailed in the table below:

|   Name      |   Discovery    | Bug | Fix  |
| :------------- |:-------------| :-----| :-----|
| Danny | When closing the class creation process early, we noticed that an event is created in the database but the survey is null. The students were able to join and see the class, but there were no surveys for them to take.| If an instructor creates an event and starts but does not finish creating a survey (i.e. reaches the number of questions but hits back to main), students can join the event and press the button to take a survey which triggers a 500 internal server error.| For both instructor and student view models, I’ve added a control flow to allow the code to check if the event has a survey associated with it. If the event does not have an associated survey, then it isn’t shown in the list of classes in the home page. Also, the student wouldn’t be able to join it even if they enter in that event id. This effectively solves the problem of being able to join survey-less events since neither users can interact with it. |
| Danny | When fixing the previously mentioned bug and testing survey-less events, we noticed that well-formed classes that were made after survey-less classes were not working as intended. | Event has a generated id and so does surveys. The way we link event to survey is to check that event and survey have the same generated id, since they both generate starting from 1 and incrementing by 1 each time. However, this creates a problem when we have events without surveys. The event has a new incremented id but the survey id is not updated with it (since it was never created to begin with). For example, the first event created does not have a survey, this event has an id of 1. The second event is created with a survey, this event has an id of 2 and a survey id of 1 associated with it. Since we compare survey id to event id in order to verify the source event, we get a 500 internal server error.| I’ve added another DatabaseField called linked_survey_id in the Event class that we can set to match with the actual survey id. This way we don’t have to assume that Event and Survey id will always match up. Finding survey’s associated event is done by comparing the event's survey’s id with the event’s linked_survey_id instead of comparing survey id to event id. Therefore, even if there are events that are created without surveys, the mismatch created from this won’t cause a 500 error.|
| Jinny | After the instructor closes the survey, then students should no longer be able to fill out the survey. However, we found out that even after the survey is closed, if a new student joins the class, they can still fill out the survey. As our recommendation assumes that all students have filled out the survey, students should not be able to fill in the survey once instructor closes it | Student vm is the frontend file that shows the list of classes to students. In iteration 4, we added a boolean data field ‘Open’ to the Survey class, so that if open is true, then the survey is opened and if open is false, the survey is closed. We used this variable to make sure that the link to the survey is not displayed to a student if it’s set to False. This condition works for those students who made accounts before the instructor closed survey, but it doesn’t work for students who joined class after the instructor closed survey.  | It was actually a simple fix - the way we set up logic in student.vm just displayed a link to a survey if a student hadn't filled in the survey yet. Even if a student has not filled in the survey yet, there should be another boolean condition that should check if the survey is open. As we already have a boolean data field ‘Open’ that indicates whether the survey is opened or not, we can use this variable to make sure that the survey is opened before displaying the link to survey. |
|Mitchell |An internal server error was sometimes thrown upon survey completion by students. We did not observe this error at the end of iteration four.|Upon investigating, I found inconsistent cookie states for users of our app. I then narrowed it down to the main sign up code not properly recording the uid cookie (which we use to keep track of the unique user id associated with your account for the database). Because this is used when filling out surveys and getting recommendations, it occasionally caused errors.|   I fixed this by ensuring the uid cookie was properly created in each sign in path. |


## Naming 
- Generally as a group, we followed our coding convention to name files and variables, so we didn’t have much to fix during the code review. Few edits were done by Vincent when he reviewed FillSurvey to fix the arr and temp cookies. He changed namings of these variables to be more descriptive. 

## Comments 

- We added docstrings to help understand the functionality of the method as it clearly defines the purpose of the function and parameters. Compared to other aspects of code review, commenting was done in a relatively inconsistent manner. We prioritize our code review so that we fix the bugs. 

## Style 
- With all of us using IntelliJ, our individual file styling tends to be relatively consistent. 
- Importantly though, we feel we did a particularly excellent job in terms of following good programming practices at the file level. A key feature of this is our structuring of our endpoints, in that one main file runs which triggers files in our endpoints folder. Our files within the folder are split logically based on what functionality the endpoints within them supports. This means that we have over a dozen separate endpoint files, which can each be read and digested easily. It also means we experience relatively few merge conflicts and have been able to improve our speed of development.

## Document 

- Josh: Updated [README](https://github.com/jhu-oose/2021-fall-group-solid/blob/main/README.md) on "How to run app on heroku" in detail 
- We recognize that ideally we would have an even more user friendly set of instructions with photos and other aids. However, even in this iteration we have added new features and refined our UI. For that reason, we avoided going down this route so that our readme would not be out of date. 









