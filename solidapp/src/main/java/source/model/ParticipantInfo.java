package source.model;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

import source.Main;

@DatabaseTable(tableName = "participants")
public class ParticipantInfo {

    @DatabaseField(canBeNull = false, unique = true)
    String name;

    @DatabaseField(canBeNull = false)
    String email;

    @DatabaseField(generatedId = true)
    int id = 0;

    @DatabaseField(canBeNull = false)
    String participantRole;

    public ParticipantInfo() {
        name = "";
        email = "";
        participantRole = "STUDENT";
    }

    /**
     * Constructor for partcipant information
     * @param name String name of the participant
     * @param email String email address of the participant
     * @param role student or instructor
     */
    public ParticipantInfo(String name, String email, String role) {
        this.name = name;
        this.email = email;
        participantRole = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {return email;}

    public Integer getId() {return id;}

    public boolean checkPermissions(String role) {
        return participantRole.equals(role);
    }

}
