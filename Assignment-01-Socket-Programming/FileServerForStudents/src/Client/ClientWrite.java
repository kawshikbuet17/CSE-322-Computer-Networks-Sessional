package Client;

import Client.Client;
import FileSendReceive.FileSendProtocol;

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
                String []arr = message.split("\\ ");
                if(arr[0].equalsIgnoreCase("upload")){
                    FileSendProtocol fileSendProtocol = new FileSendProtocol(socket, arr[2]);
                    fileSendProtocol.sendFile(arr[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
