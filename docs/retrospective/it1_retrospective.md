# Iteration 1 Retrospective

## What went well?
Our group focused our efforts on building a strong foundation on which we can build off of. With this starting point, we are hoping to be able to develop rapidly in future iterations with minimal wasted work. 

In the iteration itself, Vincent was amazing with his deliverables: he started on the database integration and even helped with other deliverables, going above and beyond to bring the project together. Mitchell wrote a great starter for our matching algorithm. It is currently in its early stages and is on a separate working branch, but it forms the basis of where we plan to go. 

## Items Delivered:
- Simple interfaces of the classes that we’ll be using later on and each respective classes that implement them
    - Added Participant interface and Student class that implements that interface
    - Created Group class that holds Students 
    - Created Instructor class 
- Sign in feature for students and the corresponding front end HTML (as long as special characters are not used)
- Simple application user interface
- Simple classes and interfaces that allow us to store surveys + determine similarity between two participants

## Items Not Delivered
- More fleshed out/detailed implementation of methods in each classes 
- Event interface that Instructor class can assign to Students

## What can we do better?

We feel that we did work, but we didn't follow the agile methodology to the extent we would like. We have work and features available on branches that are not yet integrated to our main project and the project itself does not yet provide value to our users. To improve this moving forward, we plan on each assigning and taking features from start to finish (for example, having a single person write backend and at least simple frontend code for an add survey functionality).

Another big issue that we have is our organizations. Because of midterm associated scheduling challenges, we were not able to meet to the extent we wanted. This meant we each needed to work on our separate tasks at the same time. As a result, we were not able to integrate our work fully and our project is currently split between a few folders (solidapp, our working branches etc). To solve this issue, we drafted a coding convention for our github organization. We decided to use ‘feature’ branch rules to prevent us from creating unnecessary branches. We also agreed on commit messages to organize independent commits based on the types of commits, such as adding  a new feature or fixing a bug. 

We plan on using an internal name, SOLID-Project, to store our main project, and Iteration 2 will be spent cleaning up what we had in Iteration 1, and restructuring our classes to become more fluid. 
