# Iteration 5 Retrospective

## What went well?

In this iteration, we mainly focused on refactoring code and polishing our codebase. The code review process went well and we were able to both find and fix a lot of bugs. We were able to add additional core functionality to the app, which was for students to join and leave groups by themselves after we've already placed them into groups via recommendation.

## Items delivered

- Join/leave functionality: students can leave their group and join other groups by clicking button 
- Bug fixes for edge cases such as instructor not completely filling out survey during event creation
- Frontend enhancement: updated sign in view 

## Items not delivered

- Nice-to-have functions: We haven’t implemented sign-in functionalities in this iteration. We’re still considering SIS Authentication in the future, but it would also make it harder for the grading team to test our solutions. This is because demonstrating our recommendation algorithm requires a number of accounts, and our databases cannot be manually populated due to our use of serialized fields.

## What can we do better?

- Better time management and task assignments to group members could’ve helped a lot in this project.
- We’ve learned that refactoring early can pay off dividends as it is much harder to refactor when we have interdependent classes. 
- We found with ORM Lite that at times the technology stack we use can hinder our progress, as we were not able to represent the types of relationships we wanted to. In the future we can be more intentional about selecting the tools we use at the very beginning of the project to be sure they will support our needs adequately.
- Adding comments as we go would also help other teammates to understand how our code works.


## Reflection on Iterations as a Whole:

As we progressed throughout the semester, we noticed that we were quite ambitious with our must-haves. We didn’t expect some of these features to take as much time as they did. As we were implementing, we faced a lot of technical obstacles that took time to figure out. We ended up narrowing the must-haves to our core features that would make our application viable. By honing in on a smaller number of features, it helped us focus to get our app running. For example, in our original proposal, we wanted professors to be able to confirm groups and set deadlines for group formation. We ended up having to move this feature to nice-to-have as we didn’t have enough time. We also ended up not implementing override groups features, which would let professors change groups based on their arbitration. However, our vision changed during the project, which was to primarily get the recommendation algorithm to choose the groups for us. Therefore, this feature became irrelevant to our goals. However, the core functionality (making custom surveys, taking said surveys, and forming groups) have been our main focus and was all eventually implemented on our web app.

We also noticed that the speed of our progress dramatically varies in between the start and the end of each iteration. Specifically, we as a group lacked effective measurement and intervention to keep track of everyone's progress, which usually resulted in a painful long cramming session a couple days before the deadline. One of the main causes of this phenomenon was because many of us had to learn web coding as the semester progressed and some of us needed more help than others. It would have been better to set up unofficial sub-deadlines or check points to guide everyone in the right direction rather than setting a big deadline where everyone had to present their product at the same time.

Despite these difficulties, we are proud of the accomplishments that we have had so far. We were able to apply design patterns and principles that we learned in class to our project. Whenever we had questions regarding these principles, we reached out to our TA Alan and came up with a solution together. At times technical challenges meant we needed to make hard choices to stray from more ideal designs, but even in these cases we were able to make decisions intentionally and were fully aware of the trade offs they implied. Further, each of us has gained a deeper understanding in working as a team and following agile methodologies. We feel that this experience will serve us well in our careers as we move forwards. 
