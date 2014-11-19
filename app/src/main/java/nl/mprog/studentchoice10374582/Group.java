package nl.mprog.studentchoice10374582;


import android.util.Log;

import java.util.List;
import java.util.Map;

public class Group {

    private String title;
    private String id;
    private String admin;
    private Map participants;
    private Long timestamp;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Group() {
        Log.e("Group", "Group created one1");
    }

    Group(String name, Long timestamp) {
        Log.e("Group", "Group created one2");
        this.timestamp = timestamp;
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public String getAdmin() {
        return admin;
    }

    public Long getTimestamp() {
        return timestamp;
    }

//    public void addParticipant(String name) {
//        this.participants.add(name);
//    }

    public Map getParticipants() {
        return participants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        Log.e("Group", "Setting name");
        this.id = id;
    }
}
