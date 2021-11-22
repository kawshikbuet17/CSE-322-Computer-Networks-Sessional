package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    public static List<Socket> clientSockets;
    public static HashMap<Socket, User> socketUserHashMap;
    public static int userCount = 0;

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("listening to port:5000");

        clientSockets = new ArrayList<>();
        socketUserHashMap = new HashMap<Socket, User>();

        ServerBroadcast serverBroadcast = new ServerBroadcast();
        serverBroadcast.start();

        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket+" connected");
            clientSockets.add(clientSocket);
            userCount++;
            System.out.println("user "+userCount+" connected");

            ServerClientInteraction serverClientInteraction = new ServerClientInteraction(clientSocket);
            serverClientInteraction.start();
        }
    }
}
