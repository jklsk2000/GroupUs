package source.model;

import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import source.endpoints.Daos;

@DatabaseTable(tableName = "surveys")
public class Survey {

    public static final String EVENT_ID = "event_id";

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = EVENT_ID)
    public Event e;

    @ForeignCollectionField(eager=true)
    private EagerForeignCollection<SimpleQuestion, Integer> questions;


    @ForeignCollectionField(eager=true)
    private EagerForeignCollection<SimpleSurveyResponse, Integer> responses;


    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private Integer maxPerGroup;

    @DatabaseField(canBeNull = false)
    public boolean open;

    public Survey() {   this.open = true;
    }

    public Survey(Collection<SimpleQuestion> questionCollection) {
        questions.addAll(questionCollection);
    }

    public void setMaxPerGroup(Integer val) {
        System.out.println("in setter");
        System.out.println(val);
        this.maxPerGroup = val;
        this.open = true;
    }

    public Integer getMaxPerGroup() {
        return this.maxPerGroup;
    }



    public void addQuestion(SimpleQuestion q) {
       if (this.open) {
            questions.add(q);
       }
    }

    public SimpleQuestion getQuestion(int questionNum){
        /**
         * Get an individual question.
         * @param questionNum the question to return
         * @return SurveyQuestion object corresponding to questionNum
         */
        return questions.get(questionNum);
    }

    public int length(){
        return questions.size();
    }
    public int getId(){
        return this.id;
    }

    public void setOpen(boolean isOpen) {
       open = isOpen;
    }
    public ForeignCollection<SimpleQuestion> getAllQuestions() {
        return questions;
    }

    public boolean getOpen() {
        return this.open;
    }

    public void addResponse(SimpleSurveyResponse r) {
        responses.add(r);
    }
    public SimpleSurveyResponse getResponse(int index){
        return responses.get(index);
    }

    /**
     * getter method for responses
     * @return Array list of simple survey responses
     */
    public ArrayList<SimpleSurveyResponse> getResponses() {
        ArrayList<SimpleSurveyResponse> arr = new ArrayList<SimpleSurveyResponse>();
        for (SimpleSurveyResponse r : responses) {
            arr.add(r);
        }
        return arr;
    }

    /**
     * Sets the event
     * @param e event to set to
     */
    public void setEvent(Event curr) {
        this.e = curr;
    }
    /**
     * getter method for returning the associated event
     * @return Event
     */
    public Event getSourceEvent() {
    	return e;
    }
}
