package nl.mprog.studentchoice10374582;


public class Group {

    private String title;
    private String admin;
    private String participants;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Group() {
    }

    Group(String title, String admin) {
        this.title = title;
        this.admin = admin;
    }

    public String getTitle() {
        return title;
    }

    public String getAdmin() {
        return admin;
    }
}