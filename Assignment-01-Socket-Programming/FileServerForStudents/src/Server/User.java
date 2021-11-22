package Server;

import java.util.ArrayList;
import java.util.List;

public class User {
    String username;
    List<String> privateFilenames;
    List<String> publicFilenames;

    public User() {
        privateFilenames = new ArrayList<>();
        publicFilenames = new ArrayList<>();
    }

    public void addUserName(String username){
        this.username = username;
    }

    public void addPrivateFile(String filename){
        privateFilenames.add(filename);
    }

    public void addPublicFile(String filename){
        publicFilenames.add(filename);
    }
}
