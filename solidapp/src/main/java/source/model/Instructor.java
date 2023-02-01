package source.model;

import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashSet;


@DatabaseTable(tableName = "instructors")
public class Instructor implements Participant{
    public static final String PARTICIPANTINFO_ID_FIELD_NAME = "infostore_id";

    @ForeignCollectionField(eager=true,maxEagerLevel = 5)
    private EagerForeignCollection<Event, Integer> events;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PARTICIPANTINFO_ID_FIELD_NAME, foreignAutoCreate=true)
    ParticipantInfo storedInfo;

    @DatabaseField(generatedId = true)
    int id = 0;

    public Instructor() {
        this("","");
    }

    public int getId() {
        return id;
    }

    public ParticipantInfo getStoredInfo(){return storedInfo;}

    public void setStoredInfo(ParticipantInfo s){storedInfo = s;}

    public EagerForeignCollection<Event, Integer> getEventIds() {
        return events;
    }

    public Instructor(String name, String email) {
        storedInfo = new ParticipantInfo(name, email,"INSTRUCTOR");
    }

    /**
     * getter method for getting events associated with an instructor
     * @return array of events
     */
    public ArrayList<Event> getEvents() {
        ArrayList<Event> arr = new ArrayList<Event>();
        for (Event e : events) {
            arr.add(e);
        }
        return arr;
    }


}
