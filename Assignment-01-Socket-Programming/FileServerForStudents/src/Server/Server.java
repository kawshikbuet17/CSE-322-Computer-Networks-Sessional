package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static DataOutputStream dataOutputStream = null;
    public static DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println("listening to port:5000");
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket+" connected\n");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            ServerRead serverRead = new ServerRead();
            serverRead.start();

            ServerWrite serverWrite = new ServerWrite();
            serverWrite.start();

        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
