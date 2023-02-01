package source.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "questions")
public class SimpleQuestion {

    public static final String SURVEY_ID_FIELD_NAME = "survey_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = SURVEY_ID_FIELD_NAME)
    private Survey survey;

    @DatabaseField
    private String questionText;
    /**
     * Arbitrary question text the coordinator would like displayed.
     */

    @DatabaseField
    private double weight;
    /**
     * How to weight the question. Negative values mean respondents with dissimilar responses are better matches, positive
     * values indicate respondents are better matches if they answer similarly. Magnitude indicates question importance.
     */
    @DatabaseField
    final private double maxVal;
    /**
     * Maximum value a respondent could give for the question.
     */
    @DatabaseField
    final private double minVal;
    /**
     * Min value a respondent could give for the question.
     */

    public SimpleQuestion() {
        this.weight = 0;
        this.maxVal = 5;
        this.minVal = 1;
    }

    public SimpleQuestion(Survey s, String question, double questionWeight){
        /**
         * Constructor, takes in a string a questionWeight.
         * @param question string to ask respondents
         * @param questionWeight + indicates respondents should be similar, - indicates disimilarity is better, magnitude
         * relative to other questions in the survey indicate question importance.
         */
        this.survey = s;
        this.questionText = question;
        this.weight = questionWeight;
        this.maxVal = 5;
        this.minVal = 1;

    }

    public int getId() {
        return id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey s) {
        this.survey = s;
    }


    public double getWeight() {
        /**
         * Quick getter
         * @return the weight of the question
         */
        return this.weight;
    }
    public void setWeight(double w) {
        this.weight = w;
    }

    public String getQuestionText() {
        /**
         * Quick getter
         * @return the question text to ask respondents.
         */
        return this.questionText;
    }

    public void setQuestionText(String t) {
        this.questionText = t;
    }

    public double getMaxVal() {
        /**
         * Quick getter
         * @return The maximum value a respondent could give.
         */
        return maxVal;
    }

    public double getMinVal() {
        /**
         * Quick getter
         * @return the min value a respondent could give.
         */
        return minVal;
    }
}
