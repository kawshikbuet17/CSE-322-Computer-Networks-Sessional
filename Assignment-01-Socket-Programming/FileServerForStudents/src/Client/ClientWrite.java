package Client;

import Client.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientWrite extends Thread{

    @Override
    public void run(){
        try{
            while(true){
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                Client.dataOutputStream.writeUTF(message);
                if(message.equalsIgnoreCase("exit()"))
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
