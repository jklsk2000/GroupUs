package source;

import source.endpoints.*;
import spark.Spark;

public class Main {
	public static final String STUDENT_IDENTIFIER = "STUDENT";
	public static final String INSTRUCTOR_IDENTIFIER = "INSTRUCTOR";
	
	static int PORT = 3000;

	private static int getPort() {
		String herokuPort = System.getenv("PORT");
		if (herokuPort != null) {
			PORT = Integer.parseInt(herokuPort);
		}
		return PORT;
	}
	
	private static void initEndpoints() {
		Spark.port(getPort());
		Spark.staticFileLocation("/public");

		CreateClass cc = new CreateClass();
		FillSurveyPage fp = new FillSurveyPage();
		InstructorQuestionPage iqp = new InstructorQuestionPage();
		MainPage mp = new MainPage();
		SignupPage signupPage = new SignupPage();
		SurveyPage surveyPage = new SurveyPage();
		ViewClass viewClass = new ViewClass();
		ClassResponse classResponse = new ClassResponse();
		ErrorPage errorPage = new ErrorPage();
		SignoutPage signoutPage = new SignoutPage();
		StudentGroup studentGroup = new StudentGroup();
		ModifyGroup groupModPage = new ModifyGroup();

		cc.runSpark();
		fp.runSpark();
		iqp.runSpark();
		mp.runSpark();
		signupPage.runSpark();
		surveyPage.runSpark();
		viewClass.runSpark();
		classResponse.runSpark();
		errorPage.runSpark();
		signoutPage.runSpark();
		studentGroup.runSpark();
		groupModPage.runSpark();
	}

	public static void main(String[] args) {
		initEndpoints();
	}
}
