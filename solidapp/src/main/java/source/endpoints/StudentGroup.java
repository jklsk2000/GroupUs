package source.endpoints;

import com.j256.ormlite.dao.Dao;
import source.model.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The endpoint here is used upon survey close to allow viewing which students are in which group.
 */
public class StudentGroup implements SparkSetup {

    public void runSpark() {
        Spark.get("/studentclass/:eventid", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Dao eventDao = Daos.getEventORMLiteDao();
            Dao studentDao = Daos.getStudentORMLiteDao();

            int eventId = Integer.parseInt(req.params(":eventid"));
            Event currEvent = (Event) eventDao.queryForId(eventId);
            model.put("event", currEvent);
            model.put("id", eventId);

            model.put("username", req.cookie("username"));
            model.put("role", req.cookie("role"));

            List<Student> arr = studentDao.queryForAll();
            model.put("students", arr);


            //Checking if our user is currently in a group
            ArrayList<Group> currGroups = currEvent.getGroups();
            boolean inGroup = false;
            Student currUser = (Student) studentDao.queryForId(Integer.parseInt(req.cookie("uid")));
            int currStoredId = currUser.getStoredInfo().getId();

            HashSet<Integer> groupStudents;
            for (Group g : currGroups) {
                groupStudents = g.getMembers();
                if (groupStudents.contains(currStoredId)) {
                    inGroup = true;
                }
            }
            model.put("inGroup", inGroup);

            List<Student> ls = Daos.getStudentORMLiteDao().queryForAll();

            for (Student p : ls) {
                if (p.getStoredInfo().getName().equals(URLDecoder.decode(req.cookie("username"), StandardCharsets.UTF_8.toString()))) {
                    model.put("userinfo", p);
                }
            }

            return new ModelAndView(model, "public/studentclass.vm");
        }, new VelocityTemplateEngine());
    }
}