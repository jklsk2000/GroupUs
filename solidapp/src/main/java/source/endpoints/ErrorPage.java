package source.endpoints;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

/**
 * Simple error page, used for example when users enter invalid duplicate usernames.
 */
public class ErrorPage implements SparkSetup {
	public void runSpark() {
		Spark.get("/error", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("error", URLDecoder.decode(req.cookie("error"), StandardCharsets.UTF_8.toString()));
			model.put("username", req.cookie("username"));
			model.put("role", req.cookie("role"));
			return new ModelAndView(model, "public/error.vm");
		}, new VelocityTemplateEngine());
	}
}
