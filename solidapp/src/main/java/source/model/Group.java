
package source.model;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

//import source.Utils;
import source.endpoints.Daos;
@DatabaseTable(tableName = "groups")
public class Group {

    public static final String EVENT_ID_FIELD_NAME = "event_id";

    @DatabaseField(canBeNull = false, generatedId = true)
    public int id;

    @DatabaseField()
    public int maxParticipants;

    @DatabaseField(canBeNull = false)
    public boolean open = true;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = EVENT_ID_FIELD_NAME)
    Event event;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    HashSet<Integer> students;

    public Group() {
    }

    /**
     * Constructor for group
     * @param e Event created by an instructor
     */
    public Group(Event e) {
        this.open = true;
        this.maxParticipants = e.getMaxMembersPerGroup();
        this.event = e;
        this.students = new HashSet<Integer>();
    }


    /**
     * add member(student) to the group
     * @param studentId
     * @return true if a student is added to the group, false otherwise
     */
    public boolean addMember(Integer studentId) {
        if (!open)
            return false;
        students.add(studentId);
        return true;
    }


    public boolean removeMember(Integer studentId) {
        return students.remove(studentId);
    }

    public HashSet<Integer> getMembers() {
        return students;
    }

    public int getMemberCount() {
        return students.size();
    }

    public boolean isSpace() {
        return maxParticipants < students.size();
    }

    public int getId() {
        return id;
    }

    public int getMaxmem() {
        return maxParticipants;
    }


}
