package Server;

import Client.Client;

import java.io.IOException;

public class ServerRead extends Thread{
    @Override
    public void run(){
        try{
            String message;
            while (true) {
                message = Server.dataInputStream.readUTF();
                System.out.println(message);
                if(message.equalsIgnoreCase("exit()"))
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
