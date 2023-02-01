package source.endpoints;

import com.j256.ormlite.dao.Dao;
import source.model.*;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Endpoints here are used for joining or leaving groups.
 */
public class ModifyGroup implements SparkSetup{

    public void runSpark() {
        Spark.get("/joingroup/:eventid", (req, res) -> {
            String event = req.params(":eventid");
            int eventId = Integer.parseInt(req.params(":eventid"));
            int groupId = Integer.parseInt(req.queryParams("GroupId"));
            System.out.println(groupId);
            int rawUserId = Integer.parseInt(req.cookie("uid"));


            Dao eventDao = Daos.getEventORMLiteDao();
            Dao surveyDao = Daos.getSurveyORMLiteDao();
            Dao studentDao = Daos.getStudentORMLiteDao();
            Dao groupDao = Daos.getGroupORMLiteDao();

            Student currStudent = (Student) studentDao.queryForId(rawUserId);
            int userId = currStudent.getStoredInfo().getId();

            Event currEvent = (Event) eventDao.queryForId(eventId);
            Group currGroup = (Group) groupDao.queryForId(groupId);

            currGroup.addMember(userId);
            groupDao.update(currGroup);
            eventDao.update(currEvent);

            res.status(201);

            res.redirect("/studentclass/" + currEvent.getId());
            return null;
        });
        Spark.get("/leavegroup/:eventid", (req, res) -> {
            String event = req.params(":eventid");
            int eventId = Integer.parseInt(req.params(":eventid"));

            int rawUserId = Integer.parseInt(req.cookie("uid"));

            Dao eventDao = Daos.getEventORMLiteDao();
            Dao surveyDao = Daos.getSurveyORMLiteDao();
            Dao studentDao = Daos.getStudentORMLiteDao();
            Dao groupDao = Daos.getGroupORMLiteDao();

            Student currStudent = (Student) studentDao.queryForId(rawUserId);
            int storedUserId = currStudent.getStoredInfo().getId();

            Event currEvent = (Event) eventDao.queryForId(eventId);

            //Checking if our user is currently in a group
            ArrayList<Group> currGroups = currEvent.getGroups();

            int currStoredId = currStudent.getStoredInfo().getId();
            HashSet<Integer> groupStudents;

            for (Group g : currGroups) {
                groupStudents = g.getMembers();
                if (groupStudents.contains(currStoredId)) {
                    g.removeMember(storedUserId);
                    groupDao.update(g);
                    eventDao.update(currEvent);
                }
            }

            res.status(201);

            res.redirect("/studentclass/" + currEvent.getId());
            return null;
        });


    }
}
