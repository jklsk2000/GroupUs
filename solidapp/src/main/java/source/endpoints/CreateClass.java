package source.endpoints;

import com.j256.ormlite.dao.Dao;
import source.model.Event;
import source.model.Group;
import source.model.Instructor;
import source.model.Student;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file contains endpoints used to make events. After the event is created
 * we redirect the instructor to the page that will be used to retrieve questions from
 * them and create a survey to be associated with the event created by this code.
 */
public class CreateClass implements SparkSetup {

    public void runSpark(){
        Spark.post("/createclass/:instructorid", (req, res) -> {
              Dao eventDao = Daos.getEventORMLiteDao();
              Integer id = Integer.parseInt(req.params(":instructorid"));
              Dao instructorDao = Daos.getInstructorORMLiteDao();
              Dao surveyDao = Daos.getSurveyORMLiteDao();

              Instructor creatingInstructor = (Instructor) instructorDao.queryForId(id);

              String name = req.queryParams("name");
              String description = req.queryParams("description");
              Integer maxmembers = Integer.parseInt(req.queryParams("maxmem"));

              Event newEvent = new Event(name,description,maxmembers, creatingInstructor);
              eventDao.create(newEvent);
              instructorDao.update(creatingInstructor);

              Integer eventId = newEvent.getId();
              res.status(201);
              //here we redirect to create the survey that will be associated with this event
              res.redirect("/instructorquestion/" + eventId);
              return null;
        });

        Spark.get("/createclass/:instructorid", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int id = Integer.parseInt(req.params(":instructorid"));
            model.put("id", id);
            model.put("username", req.cookie("username"));
            model.put("role", req.cookie("role"));
            return new ModelAndView(model, "public/createclass.vm");
        }, new VelocityTemplateEngine());
    }
}
