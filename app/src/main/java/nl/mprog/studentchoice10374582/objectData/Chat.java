package nl.mprog.studentchoice10374582.objectData;

public class Chat {

    private String message;
    private String author;

    // Required default constructor for Firebase object mapping
    public Chat() {
    }

    public Chat(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}