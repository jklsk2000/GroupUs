package source.endpoints;

import com.j256.ormlite.dao.Dao;
import source.model.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is one of our main endpoints, it is used for the class detail page. Importantly, as part of this, the post
 * method in this endpoint contacts our recommendation algorithm and automatically puts users into their groups upon
 * survey close.
 */
public class ViewClass implements SparkSetup {

	public void runSpark() {

		Spark.post("/class/:eventid", (req, res) -> {
			String event = req.params(":eventid");
			int id = Integer.parseInt(req.params(":eventid"));
			Dao eventDao = Daos.getEventORMLiteDao();
			Dao surveyDao = Daos.getSurveyORMLiteDao();
			Dao studentDao = Daos.getStudentORMLiteDao();
			Dao groupDao = Daos.getGroupORMLiteDao();

			Event currEvent = (Event) eventDao.queryForId(id);
			currEvent.survey.setOpen(false);
			surveyDao.update(currEvent.survey);
			eventDao.update(currEvent);

			Survey foundSurvey = currEvent.survey;

			//Build up groups in the current survey
			ArrayList<ArrayList<String>> groupIds = Recommender.matchParticipants(foundSurvey);

			Group currGroup;
			Student p;

			for (ArrayList<String> singleGroup : groupIds) {
				currGroup = new Group(currEvent);
				for (String uid : singleGroup) {
					currGroup.addMember(Integer.parseInt(uid));
				}
				groupDao.create(currGroup);
			}
			eventDao.update(currEvent);

			currEvent = (Event) eventDao.queryForId(id);
			res.status(201);

			res.redirect("/class/" + currEvent.getId());

			return null;
			});

		Spark.get("/class/:eventid", (req, res) -> {
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


			return new ModelAndView(model, "public/class.vm");
		}, new VelocityTemplateEngine());
	}
}
