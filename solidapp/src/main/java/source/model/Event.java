package source.model;
import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import source.endpoints.MainPage;

@DatabaseTable(tableName = "events")
public class Event {
    public static final String SURVEY_ID_FIELD_NAME = "survey_id";
    public static final String INSTRUCTOR_ID_FIELD_NAME = "instructor_id";

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = SURVEY_ID_FIELD_NAME)
    public Survey survey;

    @ForeignCollectionField(eager=true, maxEagerLevel = 3)
    private EagerForeignCollection<Group, Integer> groups;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    HashSet<Integer> participantIds;
    
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    HashSet<Integer> completedSurveyIds;

    @DatabaseField(generatedId = true, canBeNull = false)
    public int id;

    @DatabaseField(canBeNull = false)
    public String description;

    @DatabaseField(canBeNull = false)
    public String name;

    @DatabaseField(canBeNull = false)
    public boolean open;

    @DatabaseField(canBeNull = false)
    public int maxMembersPerGroup;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = INSTRUCTOR_ID_FIELD_NAME)
    public Instructor instructor;

    @DatabaseField
    public int linked_survey_id;

    public Event() {
        this("Event generated with default constructor", "random name", 2, null);
    }


    /**
     * Constructor for creating Event
     * @param name String name of the class
     * @param description String description of the class
     * @param maxMembers int maximum number of members for the class
     * @param i Instructor of the class
     */
    public Event(String name, String description, int maxMembers, Instructor i) {
        this.description = description;
        this.name = name;
        this.open = true;
        this.participantIds = new HashSet<Integer>();
        this.completedSurveyIds = new HashSet<Integer>();
        this.maxMembersPerGroup = maxMembers;
        this.instructor = i;
    }

    /**
    * add groups
    * @param g Group created.
     */
    public void addGroup(Group g) {
        groups.add(g);
    }

    /**
    * setter method for survey
     */
    public void setSurvey(Survey s) {
        survey = s;
    }
    public Survey getSurvey() {
        return survey;
    }

    /**
     * returns the maximum numbers per group that an instructor has set
     * @return maximum number of memebers per group
     */
    public int getMaxMembersPerGroup() {
        return maxMembersPerGroup;
    }

    /**
     * add new students to the class
     * @param studentId id related to student
     * @return true if the class is opened, false otherwise
     */
    public boolean addMember(Integer studentId) {
        if (!open)
            return false;
        participantIds.add(studentId);
        return true;
    }

    /**
     * removes member from the class
     * @param studentId id related to student
     * @return Boolean true and false
     */
    public boolean removeMember(Integer studentId) {
        return participantIds.remove(studentId);
    }

    /**
     * getter method for Ids related to the class
     * @return Ids of participants
     */
    public HashSet<Integer> getMemberIds() {
        return participantIds;
    }

    /**
     * returns the number of participants in the class
     * @return
     */
    public int getNumberParticipants() {
        return participantIds.size();
    }

    /**
     * getter method for the id of the event
     * @return event id
     */
    public int getId() {
        return id;
    }

    /**
     * gets id of the survey related to the class
     * @return
     */
    public int getLinkedSurveyId() {
        return linked_survey_id;
    }


    public void setLinkedSurveyId(int linked_survey_id) {
        this.linked_survey_id = linked_survey_id;
    }

    public String getName() {
        return name;
    }

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean isOpen) {
        open = isOpen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String s) {
        description = s;
    }
    
    public boolean containsStudentId(Integer id) {
		return participantIds.contains(id);
	}

    /**
     * add id of the student who completed the survey
     * @param studentId id of the student
     * @return true if the student's id is added properly, false otherwise
     */
    public boolean markAsCompletedSurvey(int studentId) {
    	if (takenSurvey(studentId))
            return false;
        completedSurveyIds.add(studentId);
        return true;
    }

    /**
     * boolean function that checks if a student has taken a survey or not
     * @param studentId
     * @return true if the student has completed survey, false otherwise
     */
    public boolean takenSurvey(int studentId) {
    	return completedSurveyIds.contains(studentId);
    }

    public Instructor getOwner() {
		return instructor;
	}

    /**
     * returns array of groups in the class
     * @return list of groups
     */
    public ArrayList<Group> getGroups() {
        ArrayList<Group> arr = new ArrayList<Group>();
        for (Group g : groups) {
            arr.add(g);
        }
        return arr;
    }

    /**
     * get members of the class
     * @return List of students in the class
     * @throws SQLException
     * @throws URISyntaxException
     */
    public List<Student> getMembers() throws SQLException, URISyntaxException {
		List<Student> s = new ArrayList<Student>();
		// int index = 0;
		Iterator<Integer> i = participantIds.iterator();
		while (i.hasNext()) {
			Object st = MainPage.getObjectFromID(i.next());
			if (st == null) {
				throw new IllegalArgumentException("Null student found in a group?");
			}
			if (st instanceof Instructor) {
				throw new IllegalArgumentException("A pesky instructor got in group " + name + "!!");
			}
			s.add((Student) st);
		}
		return s;
	}

    
}
