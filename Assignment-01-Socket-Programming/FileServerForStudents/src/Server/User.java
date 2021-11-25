package Server;

import java.util.ArrayList;
import java.util.List;

public class User {
    String username;
    String inbox;

    public User(String username) {
        this.username = username;
        inbox = "";
    }

    public String getUserName(){
        return username;
    }

    public void addMessage(String message){
        inbox = inbox + message + "\n";
    }
    public String checkInbox(){
        String backup = inbox;
        inbox = "";
        return backup;
    }
}
