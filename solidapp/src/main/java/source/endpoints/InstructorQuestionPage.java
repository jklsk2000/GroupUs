package source.endpoints;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import source.model.Event;
import source.model.Instructor;
import source.model.SimpleQuestion;
import source.model.Survey;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * These endpoints prompt instructors to fill in questions, and use these
 * questions to create a survey which can be linked to the correct event.
 */
public class InstructorQuestionPage implements SparkSetup {
    public void runSpark(){
        Spark.get("/instructorquestion/:eventId", (req, res) -> {
            int eventId = Integer.parseInt(req.params(":eventId"));
            List<SimpleQuestion> q = Daos.getQuestionORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("instructorquestion", q);
            model.put("eventId", eventId);

            model.put("username", req.cookie("username"));
            model.put("role", req.cookie("role"));

            return new ModelAndView(model, "public/instructorquestion.vm");
        }, new VelocityTemplateEngine());


        Spark.post("/instructorquestion/:eventId", (req, res) -> {
            //create new survey
            //should be able to post question input, create new Simple Question
            //for loop through question, append to survey
            Survey survey = new Survey();
            //System.out.println(survey.getId());
            Dao surveyDao = Daos.getSurveyORMLiteDao();
            Dao eventDao = Daos.getEventORMLiteDao();
            Integer eventId = Integer.parseInt(req.params(":eventId"));

            Event associatedEvent = (Event) eventDao.queryForId(eventId);
//            System.out.println(associatedEvent.getMaxMembersPerGroup());
            String num = req.queryParams("number");
            survey.setMaxPerGroup(associatedEvent.getMaxMembersPerGroup());
            survey.setEvent(associatedEvent);

            surveyDao.create(survey);

            int n = Integer.parseInt(num);

            for (int i = 1; i < n+1; i++) {
                //checked that queryParams work properly
                String question = req.queryParams(Integer.toString(i));
                String select = req.queryParams("select" + Integer.toString(i));
                //cast select into Double
                Double s = Double.parseDouble(select);
                SimpleQuestion q = new SimpleQuestion(survey, question, s);
                Daos.getQuestionORMLiteDao().create(q);
            }
            associatedEvent.setSurvey(survey);
            associatedEvent.setLinkedSurveyId(survey.getId());
            eventDao.update(associatedEvent);
            res.type("application/json");
            res.redirect("/");
            return new Gson().toJson(survey.toString());
        });
    }
}
