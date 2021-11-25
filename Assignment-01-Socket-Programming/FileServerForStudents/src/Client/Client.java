package Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    static int PORT1 = 8000;
    static int PORT2 = 9000;
    public static List<String> inbox;
    public static void main(String[] args) throws Exception{
        Socket socket1 = new Socket("localhost",PORT1);
        Socket socket2 = new Socket("localhost", PORT2);
        inbox = new ArrayList<>();
        ClientRead clientRead = new ClientRead(socket1, socket2);
        clientRead.start();
        ClientWrite clientWrite = new ClientWrite(socket1, socket2);
        clientWrite.start();
    }
}