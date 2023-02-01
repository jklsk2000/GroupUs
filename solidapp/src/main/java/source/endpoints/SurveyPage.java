package source.endpoints;

import source.endpoints.Daos;
import source.endpoints.SparkSetup;
import source.model.SimpleQuestion;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is for a separate survey view.
 */
public class SurveyPage implements SparkSetup {

    public void runSpark(){
        Spark.get("/survey", (req, res) -> {
            // Show all questions entered
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "public/survey.vm");
        }, new VelocityTemplateEngine());
        Spark.get("/survey", (req, res) -> {
            List<SimpleQuestion> ls = Daos.getQuestionORMLiteDao().queryForAll();
            // Show all questions entered
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("username", req.cookie("username"));
            model.put("role", req.cookie("role"));
            return new ModelAndView(model, "public/survey.vm");
        }, new VelocityTemplateEngine());
    }
}
