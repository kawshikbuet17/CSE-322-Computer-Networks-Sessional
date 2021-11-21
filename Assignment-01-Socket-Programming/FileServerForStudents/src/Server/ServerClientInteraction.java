package Server;

import Client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    @Override
    public void run(){
        try{
            String message;
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println(message);
                if(message.equalsIgnoreCase("exit()"))
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
