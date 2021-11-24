package Client;

import Client.Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientRead extends Thread{
    private DataInputStream dataInputStream;
    private Socket socket;

    public ClientRead(Socket socket) {
        this.socket = socket;
        try{
            dataInputStream = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            String message;
            message = "To log in type: login <your username>";
            System.out.println(message);
            while (true) {
                if(message.equalsIgnoreCase("logout")){
                    System.out.println("You are logged out");
                    break;
                }
                message = dataInputStream.readUTF();
                System.out.println(message);
//                Client.inbox.add(message);
            }
            dataInputStream.close();
        }catch (Exception e){
            ;
        }
    }
}
