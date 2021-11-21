package Server;

import Client.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ServerBroadcast extends Thread{
    @Override
    public void run(){
        try{
            while(true){
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                for(int i=0; i<Server.clientSockets.size(); i++){
                    if(!Server.clientSockets.get(i).isClosed()){
                        DataOutputStream dataOutputStream = new DataOutputStream(Server.clientSockets.get(i).getOutputStream());
                        dataOutputStream.writeUTF(message);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
