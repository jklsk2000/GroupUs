package source.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashSet;

import source.Main;


@DatabaseTable(tableName = "students")
public class Student implements Participant{

    public static final String PARTICIPANTINFO_ID_FIELD_NAME = "infostore_id";

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    HashSet<Integer> eventIds;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PARTICIPANTINFO_ID_FIELD_NAME, foreignAutoCreate=true)
    ParticipantInfo storedInfo;

    @DatabaseField(generatedId = true)
    int id = 0;

    public Student() {
        this("","");
    }

    public Student(String name, String email) {
        storedInfo = new ParticipantInfo(name, email,"STUDENT");
        eventIds = new HashSet<Integer>();
    }

    public int getId() {
        return id;
    }

    public ParticipantInfo getStoredInfo(){return storedInfo;}

    public void setStoredInfo(ParticipantInfo s){storedInfo = s;}


}