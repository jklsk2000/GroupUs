package source.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "responses")
public class SimpleSurveyResponse{
    public static final String SURVEY_ID_FIELD_NAME = "survey_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    int[] answers;

    @DatabaseField
    int numQuestions;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = SURVEY_ID_FIELD_NAME)
    Survey survey;

    @DatabaseField
    String username;

    @DatabaseField
    Integer respondentId;

    public SimpleSurveyResponse() {
    }
    public SimpleSurveyResponse(Survey s, String name, Integer userId) {
        username = name;
        respondentId = userId;
        setSurvey(s);
    }

    public Integer getId(){
        return respondentId;
    }

    public void setUsername(String u) {
        username = u;
    }
    public String getUsername(){
        return username;
    }
    public void setSurvey(Survey s) {
        survey = s;
        numQuestions = s.length();
        answers = new int[numQuestions];
    }
    public Survey getSurvey(){
        return survey;
    }

    /**
     * make sure that answer is greather than the min val and less than max val
     * @param index integer
     * @param value In
     */
    public void answerQuestion(int index, Integer value){
        //TODO: remove asserts and replace with throw
        assert value >= survey.getQuestion(index).getMinVal();
        assert value <= survey.getQuestion(index).getMaxVal();
        answers[index] = value;
    }
    public float getAnswer(int index) {
        /**
         * Get individual answer values
         * @param index of question to get answer to
         * @return float value representing raw answer to the question.
         */
        return (float) answers[index];
    }
    public int length(){
        return answers.length;
    }

    public int[] getAllAnswers() {
        return answers;
    }
}
