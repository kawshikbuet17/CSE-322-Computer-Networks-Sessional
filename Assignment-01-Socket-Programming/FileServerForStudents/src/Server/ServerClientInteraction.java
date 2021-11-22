package Server;

import Client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ServerClientInteraction extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;

    public ServerClientInteraction(Socket socket) {
        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void createFolder(String folderName){
        File file = new File("Storage");
        file.mkdir();
        file = new File("Storage/"+folderName);
        file.mkdir();
        file = new File("Storage/"+folderName+"/public");
        file.mkdir();
        file = new File("Storage/"+folderName+"/private");
        file.mkdir();
    }

    @Override
    public void run(){
        try{
            String message;
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println(message);
                String []arr = message.split("\\ ");
                if(arr[0].equalsIgnoreCase("login")){
                    User user = new User();
                    user.addUserName(arr[1]);
                    Server.socketUserHashMap.put(socket, user);
                    System.out.println("username : " + arr[1]);
                    createFolder(arr[1]);
                }
                if(message.equalsIgnoreCase("exit()"))
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
