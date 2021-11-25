package Server;

import java.util.ArrayList;
import java.util.List;

public class User {
    String username;
    List<String> privateFilenames;
    List<String> publicFilenames;

    public User(String username) {
        this.username = username;
    }

    public String getUserName(){
        return username;
    }
}
