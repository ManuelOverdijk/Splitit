package nl.mprog.studentchoice10374582;

import com.firebase.client.AuthData;

public class User {
    private String          name;
    private String          email;
    private boolean         isActive;
    private AuthData        authData;
    private String          uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}