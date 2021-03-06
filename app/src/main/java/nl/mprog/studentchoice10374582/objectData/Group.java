package nl.mprog.studentchoice10374582.objectData;


import java.util.Date;
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

    }

    Group(String title, String admin) {
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
