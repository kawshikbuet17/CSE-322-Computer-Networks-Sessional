package Client;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientWrite extends Thread{
    private Socket socket1;
    private Socket socket2;
    private DataOutputStream dataOutputStream;
    public ClientWrite(Socket socket1, Socket socket2) {
        this.socket1 = socket1;
        this.socket2 = socket2;
    }
    @Override
    public void run(){
        try{
            while(true){
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                dataOutputStream = new DataOutputStream(socket1.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                String []arr = message.split("\\ ");
                if(arr[0].equalsIgnoreCase("upload")){
                    FileSendWithAck fileSendWithAck = new FileSendWithAck(socket2, arr[1], arr[2]);
                    fileSendWithAck.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
