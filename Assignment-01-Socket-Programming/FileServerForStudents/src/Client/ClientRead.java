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