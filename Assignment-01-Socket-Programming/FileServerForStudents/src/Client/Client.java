package Client;

import FileManagement.ClientDownload;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static List<String> inbox;
    public static ClientDownload clientDownload;
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost",5000);
        Socket downloadSocket = new Socket("localhost", 6000);
        inbox = new ArrayList<>();
        ClientRead clientRead = new ClientRead(socket);
        clientRead.start();
        ClientWrite clientWrite = new ClientWrite(socket);
        clientWrite.start();

        clientDownload = new ClientDownload(downloadSocket);
    }
}