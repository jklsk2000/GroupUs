package source.endpoints;
import source.Main;
import source.model.*;
import spark.ModelAndView;
import spark.Request;
import spark.Spark;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.template.velocity.VelocityTemplateEngine;


/**
 * Our main launch page.
 */
public class MainPage implements SparkSetup {

	/**
	 * Simple helper to ensure users have all the cookies they need to be potentially logged in.
	 * @param req request the get call was called with
	 * @return true if the user has all necessary cookies
	 */
	public boolean hasAllCookies(Request req) {
		boolean hasUsername = req.cookie("username") != null;
		boolean hasRole = req.cookie("role") != null;
		boolean hasUID = req.cookie("uid") != null;
		return hasUsername && hasRole && hasUID;
	}

	/**
	 * Quick helper method to retrieve a user based on our cookies
	 * @param arr list of participants (either students or instructors)
	 * @param req our current request
	 * @return Participant if the username wsa found, otherwise null.
	 * @throws UnsupportedEncodingException if the username is not able to be converted to a string.
	 */
	public Participant checkForUser(List<Participant> arr, Request req) throws UnsupportedEncodingException {
		String currentUsername =  URLDecoder.decode(req.cookie("username"), StandardCharsets.UTF_8.toString());
		boolean loggedIn = false;
		for (Participant p : arr) {
			if (p.getStoredInfo().getName().equals(currentUsername)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Method to run our endpoints. 
	 */
    public void runSpark() {
        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            ModelAndView mv = new ModelAndView(model,"public/index.vm");

			//check if all cookies are present and the user is potentially logged in
            if (this.hasAllCookies(req)) {
                model.put("username", req.cookie("username"));
                boolean loggedIn = false;
				List<SimpleSurveyResponse> ls_r = Daos.getResponseORMLiteDao().queryForAll();
                List<Student> ls_e = Daos.getEventORMLiteDao().queryForAll();
                model.put("events", ls_e);

				// if their role cookie indicates they're a student
                if (req.cookie("role").equals(Main.STUDENT_IDENTIFIER)) {
                    model.put("role",req.cookie("role"));
					List<Participant> ls = Daos.getStudentORMLiteDao().queryForAll();

					// make sure this student is present in our db + put them in the model
					Participant p = this.checkForUser(ls,req);
					if (p != null) {
						loggedIn = true;
						model.put("userinfo",(Student) p);
					}
                    mv = new ModelAndView(model, "public/student.vm");

				//	if their cookie indicates they're an instructor
                } else if (req.cookie("role").equals(Main.INSTRUCTOR_IDENTIFIER)) {
                    model.put("role",req.cookie("role"));
                    List<Participant> ls = Daos.getInstructorORMLiteDao().queryForAll();
					Participant p = this.checkForUser(ls,req);
					//same here but for instructors
                    if (p!= null) {
						loggedIn = true;
						model.put("instructor", (Instructor) p);
					}
                    mv = new ModelAndView(model, "public/instructor.vm");

                } else {
                    // remove their invalid cookies and send them to log in
                	res.removeCookie("username");
					res.removeCookie("role");
					res.removeCookie("uid");
					res.redirect("/");
					return null;
                }

                if (!loggedIn) {
                    //they set their cookies but didn't exist in the db. Remove cookies and make them log in again
                	res.removeCookie("username");
					res.removeCookie("role");
					res.removeCookie("uid");
					res.redirect("/");
					return null;
                }
            }
            return mv;
        }, new VelocityTemplateEngine());



        Spark.post("/", (req, res) -> {
			List<Student> ls_s = Daos.getStudentORMLiteDao().queryForAll();
			List<Instructor> ls_i = Daos.getInstructorORMLiteDao().queryForAll();

			String username = req.cookie("username");
			if (username != null)
				username = URLDecoder.decode(username, StandardCharsets.UTF_8.toString());
			boolean joiningClass = false;
			String classnum = req.queryParams("classnum");
			if (classnum != null)
				joiningClass = true;
			if (joiningClass) {
				//for now, the only post request with a non-null username is from a student joining a class.
				res.removeCookie("posttype");
				String num = classnum;
				try {
					int classId = Integer.parseInt(num);
					Object user = getObjectFromUsername(username);
					if (user instanceof Instructor) {
						res.cookie("error", URLEncoder.encode("Instructors aren't able to join classes according to design doc", StandardCharsets.UTF_8.toString()));
						res.redirect("/error");
						return null;
					}
					Event e = (Event) Daos.getEventORMLiteDao().queryForId(classId);
					if (e.getSurvey() != null) {
						e.addMember(((Student) user).getStoredInfo().getId());
						Daos.getEventORMLiteDao().update(e);
					}
				} catch (Exception e) {
					res.cookie("error", URLEncoder.encode("Bad classroom ID. Please try again.", StandardCharsets.UTF_8.toString()));
					res.redirect("/error");
					return null;
				}
			} else {
				res.removeCookie("posttype");
				username = req.queryParams("username");
				boolean success = false;
				int role = -1; // -1 null, 0 student, 1 instructor
				for (Student p : ls_s) {
					if (p.getStoredInfo().getName().equals(username)) {
						success = true;
						res.cookie("uid","" + p.getId());
						role = 0;
					}
				}
				for (Instructor p : ls_i) {
					if (p.getStoredInfo().getName().equals(username)) {
						success = true;
						res.cookie("uid","" + p.getId());
						role = 1;
					}
				}
				if (success) {
					res.cookie("username", URLEncoder.encode(username, StandardCharsets.UTF_8.toString()));
					res.cookie("role", role == 0 ? Main.STUDENT_IDENTIFIER : Main.INSTRUCTOR_IDENTIFIER);
				} else {
					if (username.equals("")) {
						System.err.println("Empty username...");
						res.cookie("error", URLEncoder.encode("Please enter a username...", StandardCharsets.UTF_8.toString()));
						res.redirect("/error");
					} else {
						System.err.println("No account found for " + username);
						res.cookie("error", URLEncoder.encode("No account found for \"" + username + "\"", StandardCharsets.UTF_8.toString()));
						res.redirect("/error");
					}
					return null;
				}
			}
			res.redirect("/");
			return null;
		});

    }
    
    /**
	 * <pre>
	 * Return an array of two objects, a boolean at index 0 and integer at index 1. The boolean is
	 * whether the user with the name was found, and the role is the integer:
	 * -1 = null, 0 = student, 1 = instructor
	 * </pre>
	 * 
	 * @param name - username to check for
	 * @return Object array with boolean and integer
	 * @throws SQLException
	 */
	public static Object[] hasUserWithName(String name) throws SQLException, URISyntaxException {
		
		List<Student> ls_s = Daos.getStudentORMLiteDao().queryForAll();
		List<Instructor> ls_i = Daos.getInstructorORMLiteDao().queryForAll();
		
		boolean success = false;
		int role = -1; // -1 null, 0 student, 1 instructor
		for (Student p : ls_s) {
			if (p.getStoredInfo().getName().equals(name)) {
				success = true;
				role = 0;
			}
		}
		for (Instructor p : ls_i) {
			if (p.getStoredInfo().getName().equals(name)) {
				success = true;
				role = 1;
			}
		}
		return new Object[] {success, role};
	}
	
	/**
	 * Get either a student or instructor object from their username. Incredibly inefficient...
	 * 
	 * @param name - username
	 * @return relevant Student or Instructor object
	 * @throws SQLException
	 */
	public static Object getObjectFromUsername(String name) throws SQLException, URISyntaxException {
		
		List<Student> ls_s = Daos.getStudentORMLiteDao().queryForAll();
		List<Instructor> ls_i = Daos.getInstructorORMLiteDao().queryForAll();

		for (Student p : ls_s) {
			if (p.getStoredInfo().getName().equals(name)) {
				return p;
			}
		}
		
		for (Instructor p : ls_i) {
			if (p.getStoredInfo().getName().equals(name)) {
				return p;
			}
		}
		
		return null;
		
	}
	
	/**
	 * Get either a student or instructor object from their id. Incredibly inefficient...
	 * 
	 * @param id - participant id
	 * @return relevant Student or Instructor object
	 * @throws SQLException
	 */
	public static Object getObjectFromID(Integer id) throws SQLException, URISyntaxException {
		
		List<Student> ls_s = Daos.getStudentORMLiteDao().queryForAll();
		List<Instructor> ls_i = Daos.getInstructorORMLiteDao().queryForAll();

		for (Student p : ls_s) {
			//System.out.println(p.getStoredInfo().getName() + " - " + p.getStoredInfo().getId());
			
			if (p.getStoredInfo().getId().equals(id)) {
				return p;
			}
		}
		
		for (Instructor p : ls_i) {
			//System.out.println(p.getStoredInfo().getName() + " - " + p.getStoredInfo().getId() + " - " + p.getId());
			
			if (p.getStoredInfo().getId().equals(id)) {
				return p;
			}
		}
		
		return null;
		
	}
	
	/**
	 * Get an instructor object from their id. Incredibly inefficient...
	 * 
	 * @param id - Instructor ID
	 * @return relevant Instructor object
	 * @throws SQLException
	 */
	public static Object getObjectFromInstructorID(Integer id) throws SQLException, URISyntaxException {
		
		List<Instructor> ls_i = Daos.getInstructorORMLiteDao().queryForAll();
		
		for (Instructor p : ls_i) {
			//System.out.println(p.getStoredInfo().getName() + " - " + p.getStoredInfo().getId() + " - " + p.getId());
			
			if (id.equals(p.getId())) {
				return p;
			}
		}
		
		return null;
		
	}

    
    

}
