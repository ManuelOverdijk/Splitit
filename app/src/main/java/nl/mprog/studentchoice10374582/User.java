package nl.mprog.studentchoice10374582;

/* Created by Manuel 11/08/2014
   Default User
*/

import com.firebase.client.AuthData;

public class User {

    private String email;
    private String name;
    private String provider;
    private String userid;

    private AuthData authData;

    // Required default constructor for Firebase object mapping
    private User() { }

    User(String userid) {
        this.userid = userid;
    }


    User(AuthData authData, String email, String name) {
        this.authData = authData;
        this.userid = authData.getUid();
        this.name = name;
        this.provider = authData.getProvider();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProvider() {
        return provider;
    }

    public String getUserId() {
        return userid;
    }

    public AuthData getAuthData() { return authData;}

}