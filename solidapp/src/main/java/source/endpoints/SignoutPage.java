package source.endpoints;

import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

/**
 * Simple signout page.
 */
public class SignoutPage implements SparkSetup {
	public void runSpark() {
		Spark.get("/signout", (req, res) -> {
			res.removeCookie("username");
			res.removeCookie("role");
			res.removeCookie("uid");
			res.status(201);
			res.redirect("/");
			return null;
		}, new VelocityTemplateEngine());
	}
}
