package Server;

public class Request {
    User user;
    String filename;

    public Request(User user, String filename) {
        this.user = user;
        this.filename = filename;
    }

    public User getUser() {
        return user;
    }

    public String getFilename() {
        return filename;
    }
}
