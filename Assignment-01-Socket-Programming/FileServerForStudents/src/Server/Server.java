package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Server {
    static int PORT1 = 8000;
    static int PORT2 = 9000;
    public static List<Socket> clientSockets1;
    public static List<Socket> clientSockets2;
    public static HashMap<Socket, User> socketUserHashMap1;
    public static HashMap<Socket, User> socketUserHashMap2;
    public static List<Request> requests;
    public static List<String> singleUser;
    public static int userCount = 0;

    static int MAX_CHUNK_SIZE = 4*1024;
    static int MIN_CHUNK_SIZE = 1024;

    static int MAX_BUFFER_SIZE = 2000000000;
    static int AVAILABLE_BUFFER_SIZE = MAX_BUFFER_SIZE;

    public static int genChunkSize(){
        Random random = new Random();
        int temp = random.nextInt(MAX_CHUNK_SIZE-MIN_CHUNK_SIZE);
        return temp + MIN_CHUNK_SIZE;
    }

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket1 = new ServerSocket(PORT1);
        ServerSocket serverSocket2 = new ServerSocket(PORT2);
        System.out.println("listening to port: "+ PORT1 + " , " + PORT2);

        clientSockets1 = new ArrayList<>();
        clientSockets2 = new ArrayList<>();
        socketUserHashMap1 = new HashMap<Socket, User>();
        socketUserHashMap2 = new HashMap<Socket, User>();
        singleUser = new ArrayList<>();

        requests = new ArrayList<Request>();


        ServerBroadcast serverBroadcast = new ServerBroadcast();
        serverBroadcast.start();

        while(true){
            Socket socket1 = serverSocket1.accept();
            System.out.println(socket1+" connected");
            clientSockets1.add(socket1);

            Socket socket2 = serverSocket2.accept();
            System.out.println(socket2+" download socket connected");
            clientSockets2.add(socket2);

            userCount++;
            System.out.println("user "+userCount+" connected");

            ServerClientInteraction serverClientInteraction = new ServerClientInteraction(socket1, socket2);
            serverClientInteraction.start();
        }
    }
}
