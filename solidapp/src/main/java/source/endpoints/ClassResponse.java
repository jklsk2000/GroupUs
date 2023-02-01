package source.endpoints;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import source.model.SimpleSurveyResponse;
import source.model.Survey;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an endpoint that allows us to view all responses to the survey associated
 * with a given class.
 */
public class ClassResponse implements SparkSetup{

    public void runSpark(){
        Spark.get("/classresponse/:surveyid", (req, res) -> {
            Dao responseDao = Daos.getResponseORMLiteDao();
            int id = Integer.parseInt(req.params(":surveyid"));
            List<SimpleSurveyResponse> responses = responseDao.queryForEq("survey_id", id);
            for (SimpleSurveyResponse r : responses){
                System.out.println(r.getAllAnswers());
            }
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("responses", responses);
            model.put("username", req.cookie("username"));
            model.put("role", req.cookie("role"));
            return new ModelAndView(model, "public/classresponse.vm");
        }, new VelocityTemplateEngine());
    }
}
