package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static DataOutputStream dataOutputStream = null;
    public static DataInputStream dataInputStream = null;
    public static Socket socket = null;

    public static void Initialize() {
        try {
            socket = new Socket("localhost",5000);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Initialize();
        ClientRead clientRead = new ClientRead();
        clientRead.start();
        ClientWrite clientWrite = new ClientWrite();
        clientWrite.start();
    }
}