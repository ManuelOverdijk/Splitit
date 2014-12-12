package nl.mprog.studentchoice10374582;


import android.util.Log;

import java.util.Date;
import java.util.Map;

public class Bill {

    private String title;
    private String id;
    private String admin;
    private Map participants;
    private Long created;
    private Boolean completed;
    private Long lastUpdated;


    /* Used for Firebase Object mapping */
    public Bill() {}


    Bill(String title, String admin) {
        this.title = title;
        this.admin = admin;
    }


    public String getTitle() {
        return title;
    }

    public String getAdmin() {
        return admin;
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

    public void setCompleted(Boolean c){
        this.completed = c;
    }

    public Boolean getCompleted(){
        return completed;
    }

    public void setLastUpdated(Long c){
        this.lastUpdated = c;
    }

    public Long getLastUpdated(){
        return lastUpdated;
    }
}
