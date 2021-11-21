package Client;

import Client.Client;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientRead extends Thread{
    @Override
    public void run(){
        try{
            String message;
            while (true) {
                message = Client.dataInputStream.readUTF();
                System.out.println(message);
                if(message.equalsIgnoreCase("exit()"))
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
