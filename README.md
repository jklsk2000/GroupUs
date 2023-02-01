# TEAM SOLID presents:

# GroupUs
### ***Creating groups of people out of the blue are a mixed bag.*** 
### ***Experiences with poorly made groups have influenced us at Team SOLID to create a streamlined solution to group creation.***



<img width="1295" alt="Screen Shot 2021-12-06 at 9 41 35 PM" src="https://user-images.githubusercontent.com/70327883/144956415-86a3e3c8-1cea-45e7-ab98-6e1f5ee75740.png">









## Functionality
Our app allows users to sign up and log in as an Instructor or Student. 

Depending on which account type was chosen, the user will be given the option to join a class (Student) or create a class (Instructor).

For Instructors, they can create a class with a name, description, and number of participants per group, and subsequently be directed to create a survey for that group. They can then share the group by showing students the event ID, which allows students to join that group. Students can then take the survey on their end, and the instructor can then view the responses by clicking "View event details..." at the bottom of each group, then choosing "View survey responses".

We have also developed the basis of functionality for negative weights for questions, improved overall appearance of the app, and added error handling. However, please note that recommendation has not been implemented fully yet.

#### For iteration 4, we mainly focused on:

We focused on getting our base functionality completed for the fourth iteration. This means we worked on students joining groups, site appearance (such as a new navigation bar), basic error handling, taking surveys and viewing results, the reccomendation algorithm, and Heroku deployment. We are in a much better position compared to iteration 3 to focus on smaller things like tidying up error handling, fine-tuning the reccomendation algorithm, and generally improving the quality of the front-end.

#### Some caveats to our application:

We have not throughly debugged our application to the extent we would like, and we currently have no automatic testing. This means some behavior may be a bit odd or bugs may occur at times—we intend to iron out as many of these as possible for iteration 5. Additionally, groups are created via the recommendation algorithm, but we currently do not take advantage of some of the functionality they offer (ie letting instructors allow students to join or leave as desired).

## How to Run our App locally 
1. Download the solidapp folder to your computer.
2. If you don't have it beforehand, download [OpenJDK Version 8](https://adoptium.net/?variant=openjdk8&jvmVariant=hotspot). 
3. Open the folder in a Java IDE (IntelliJ recommended).
4. Set the Project SDK to the OpenJDK above (in IntelliJ, File > Project Structure > Project > Project SDK).
5. From the build.gradle file, build and run the project, pay attention to the output to ensure it is deployed via localhost.
6. Run the application!
7. In your browser, type [localhost:3000](https://localhost:3000) in the URL, or use this link. 
8. *IMPORTANT* Ensure that any cookies (and databases, if running locally) from previous times accessing the app are deleted (otherwise you may run into various errors).
9. Create a new account or sign into an existing account. At this point you will indicate if you are an instructor or a student.
10. From here you can either create a room and survey as an instructor, or join one you've made as a student.
11. From there, you can take the survey as a student account(s).
12. Now you can view the responses from your instructor account.

## Run our App through Heroku!

Our app is now available to visit through this link: https://oosesolidapp.herokuapp.com/

**If you are an instructor.**
1. Create an instructor account.
2. Create an event and edit its description and set the upper limit for the maximum number of students per each group.
3. Create a survey for the event. Set the number of questions needed for the survey and their content.
4. Distribute the designated event ID to students so that they can join.
5. Check the current student responses by clicking View Survey Responses button.
6. Close the survey to get the group distribution result from the recommendation algorithm.

**If you are a student.**
1. Create a student account.
2. Type in the event ID of the event that you are trying to join from your instructor (Instructor should sent this code to you via email or campuswire).
3. Click "Take Survey for Placement!" to take the placement survey.
4. Click "Checkout the recommended groups!" to view your recommended group if it is made available by the instructor.
5. Click Join/Leave button to leave and join another if you desire.

## What Our App Uses
Our app is primarily Java for the backend, and HTML/CSS/JS for frontend.

The APIs in use are as follows:
- SQLite
- ORMLite 
- SparkJava
- Heroku

Future APIs to be Implemented:
- PostGRES: In the future, our use of SQLite in our code will be phased out in favor of PostGRES for compatability with Heroku.

## Stop fumbling with poorly made groups. Group Productively. GroupUs.

### Team Members:
- Kyoungjin Lim
- Jaekyun (Danny) Lee
- Jonathon Negron
- Euikwang (Josh) Kim
- Mitchell Pavlak
- Vincent Providence

### Coding Convention 
- [📚 coding convention](https://github.com/jhu-oose/2021-fall-group-solid/blob/main/convention.md)


#### Team SOLID: We're a SOLID team!
