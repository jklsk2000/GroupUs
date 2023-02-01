# Iteration 4 Retrospective

## What went well?
I think that a lot of things that were implemented in the backend in the past iterations have been able to be shown through by updating our frontend in this iteration. We are now able to display negative weights for the survey questions, classes created by instructors now show up in the front page, and the general flow of our website is much more intuitive.

## Items Delivered
- Web application was deployed via Heroku
- The database was integrated to use Postgresql along with SQLite to make it compatible with Heroku
- Page flows for instructors and students were split
- Instructors now can make events and create associated surveys
- Students can join classes via entering an id the instructor would provide.
- After joining classes, students can fill out surveys
- Frontend now supports negative weights for the survey questions, so we can support questions where answers should be either similar or dissimilar 
- Instructors can close surveys and use a recommendation algorithm to place students into groups automatically
- Students can view the group that they have been assigned along with other groups formed by the Recommender algorithm

## Items Not Delivered
- Students aren’t yet able to join and leave groups on their own, only the Instructor has the ability to form groups through the recommendation algorithm (we’ll move this into nice-to-have)
- Cookie clean-up: We haven't implemented automatic cookie clean up functionality in this iteration. 
- We have not decided whether to use Google OAuth or Hopkins SSO sign in.

## What can we do better?
Better communication between members about what tasks we are getting done so we don’t repeat the tasks. We discovered that having separate branches for all of us has been a struggle when it came to merging everyone’s branch together into the main branch. I think we could do better by pushing our changes more often. I think it might be more efficient for us to push to a shared branch instead for fast integration.
