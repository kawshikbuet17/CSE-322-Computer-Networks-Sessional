package Server;

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
                for(int i = 0; i<Server.clientSockets1.size(); i++){
                    if(!Server.clientSockets1.get(i).isClosed()){
                        DataOutputStream dataOutputStream = new DataOutputStream(Server.clientSockets1.get(i).getOutputStream());
                        dataOutputStream.writeUTF(message);
                        dataOutputStream.flush();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
