package FileManagement;

import Server.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

public class FileViewProtocol {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public FileViewProtocol(Socket socket) throws Exception{
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void viewFiles(User user) throws Exception{
        String message = "";
        File dir = new File("Storage");
        String []children = dir.list();

        for(int i=0; i<children.length; i++){
            if(children[i].equalsIgnoreCase(user.getUserName())){
                dir = new File("Storage/"+children[i]+"/"+"private");
                String []privateFiles = dir.list();

                for(int j=0; j<privateFiles.length; j++){
                    message+= "Storage/"+children[i]+"/"+"private/"+privateFiles[j]+"\n";
                }
            }

            dir = new File("Storage/"+children[i]+"/"+"public");
            String []publicFiles = dir.list();

            for(int j=0; j<publicFiles.length; j++){
                message+= "Storage/"+children[i]+"/"+"public/"+publicFiles[j]+"\n";
            }
        }
        dataOutputStream.writeUTF(message);
    }
}
