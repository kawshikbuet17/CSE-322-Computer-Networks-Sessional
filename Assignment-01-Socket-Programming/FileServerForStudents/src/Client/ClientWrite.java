package Client;

import Client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientWrite extends Thread{
    private Socket socket;
    private DataOutputStream dataOutputStream;
    public ClientWrite(Socket socket) {
        this.socket = socket;
        try{
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        try{
            while(true){
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                dataOutputStream.writeUTF(message);
                if(message.equalsIgnoreCase("exit()"))
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
