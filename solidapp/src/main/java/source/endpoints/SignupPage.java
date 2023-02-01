package source.endpoints;


import source.Main;
import source.model.Instructor;
import source.model.Student;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoints and helpers for letting users log in.
 */
public class SignupPage implements SparkSetup {

    public void runSpark(){
        Spark.get("/signup", (req, res) -> {
            List<Student> ls = Daos.getStudentORMLiteDao().queryForAll();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("students", ls);
            return new ModelAndView(model, "public/signup.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/signup", (req, res) -> {
            String name = req.queryParams("name");
            String email = req.queryParams("email");
            String who = req.queryParams("whoami");
            System.out.println(name);
            System.out.println(email);
            System.out.println(who);
            
            if (!validUserEmail(email) || (Boolean) (MainPage.hasUserWithName(name)[0])) {
				res.cookie("error", URLEncoder.encode("Invalid username or email: Duplicate", StandardCharsets.UTF_8.toString()));
				res.redirect("/error");
				res.removeCookie("username");
				res.removeCookie("role");
			}

            if (who.equals("Student")) {
                Student p = new Student(name, email);
                Daos.getStudentORMLiteDao().create(p);
                res.cookie("role", Main.STUDENT_IDENTIFIER);
                res.cookie("uid","" + p.getId());
            } else {
                Instructor p = new Instructor(name, email);
                Daos.getInstructorORMLiteDao().create(p);
                res.cookie("role", Main.INSTRUCTOR_IDENTIFIER);
                res.cookie("uid","" + p.getId());
            }

            res.status(201);
            res.cookie("username", URLEncoder.encode(name, StandardCharsets.UTF_8.toString()));
            res.redirect("/");
            return null;
        });
    }
    
    public boolean validUserEmail(String email) {
		try {
			for (String s : queryAllEmails()) {
				if (s.equals(email))
					return false;
			}
		} catch (SQLException | URISyntaxException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public String[] queryAllEmails() throws SQLException, URISyntaxException {
		List<Student> st_s = Daos.getStudentORMLiteDao().queryForAll();
		List<Instructor> st_i = Daos.getInstructorORMLiteDao().queryForAll();
		String[] arr = new String[st_s.size() + st_i.size()];
		
		for (int i = 0; i < st_s.size(); i++) {
			arr[i] = st_s.get(i).getStoredInfo().getEmail();
		}
		for (int i = 0; i < st_i.size(); i++) {
			arr[i + st_s.size()] = st_i.get(i).getStoredInfo().getEmail();
		}
		
		return arr;
	}

}
