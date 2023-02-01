## Iteration 2 Retrospective
What went well?
Previously we had issues where a large amount of our work for iteration one was not fully integrated. This happened because the tasks we assigned were not fully end-to-end—we had tasks that involved features that depended on future work to be integrated into a cohesive application. The result was that our work for the iteration was spread among a few active branches and folders.

 In order to move forward, we needed to regroup and merge our code. We also focused on assigning tasks that we can each take from start to finish to ensure we have shippable work. This worked out well in terms of the number of relatively large tasks we were able to ship, from persisting surveys to responding to them and outlines for manual group creation. To tie it all together, we started to draft our front end, and started production of a wireframe to assist in the design of both ends. 

## Items Delivered:
Cleaned up a lot of the flaws in our code base from iteration 1 and merged most of our work in
Cleaned up and organized our file structure a bit
Refactoring code to follow MVC pattern
Adding ‘create survey’ feature to Instructor class 
Front-end: Instructor VM showing the survey creation page 
Back-end: Storing the instructor’s question input to the database 
Persisting surveys with an arbitrary number of questions and responses
Responding surveys with an arbitrary number of questions + storing responses
Basis of functionality to form classes and manually create groups (visible on our app though not all operations are fully functional)

## Items Not Delivered: 
We have not yet fully integrated our create survey feature with our updated Survey class we delivered this iteration

## What can we do better?
While we did improve in terms of shipping features, we still had some rough points in terms of making sure features were fully integrated. In particular, we ran out of time before the end of iteration 2 to let survey creation functionality support our updated persisted survey class with an arbitrary number . That being the case, because of our approach at ensuring each task is deliverable, we were still able to deploy endpoints on our app to demonstrate how surveys can be created though users are not yet able to respond to them. Since we learned about “Continuous Integration” in class, we realized we could address this by integrating each member’s code regularly during the iteration. 

On a similar note, merging and integrating different branches took a lot of time. We gathered in-person and tried to integrate it close to the last day of the iteration, and it took longer to resolve merge conflicts than we expected. We expect that attempting a continuous integration style workflow will also help resolve this, and it is one of our goals for this iteration.
