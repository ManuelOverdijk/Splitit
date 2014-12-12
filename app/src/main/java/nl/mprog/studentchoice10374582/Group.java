package nl.mprog.studentchoice10374582;


import android.util.Log;

import java.util.Date;
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
    public Group() {
        Log.e("Group", "Group created one1");
    }

    Group(String title, String admin) {
        Log.e("Group", "Group created one2");
        this.timestamp = new Date().getTime();
        this.title = title;
        this.admin = admin;
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

    public void setParticipants(Map part){
        this.participants = part;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAdmin(String admin){
        this.admin = admin;
    }
}
