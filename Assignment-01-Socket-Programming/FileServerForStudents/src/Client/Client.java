package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost",5000);
        ClientRead clientRead = new ClientRead(socket);
        clientRead.start();
        ClientWrite clientWrite = new ClientWrite(socket);
        clientWrite.start();
    }
}