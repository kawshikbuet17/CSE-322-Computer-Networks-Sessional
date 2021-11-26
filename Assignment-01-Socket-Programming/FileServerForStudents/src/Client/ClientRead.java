package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientRead extends Thread{
    private DataInputStream dataInputStream;
    private Socket socket1;
    private Socket socket2;

    public ClientRead(Socket socket1, Socket socket2) {
        this.socket1 = socket1;
        this.socket2 = socket2;
        try{
            dataInputStream = new DataInputStream(socket1.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            String message;
            message = "To log in type: login <your username>";
            System.out.println(message);
            while (true) {
                if(socket1.isClosed() || socket2.isClosed()){
                    break;
                }
                message = dataInputStream.readUTF();
                String []arr = message.split("\\ ");
                if(message.equalsIgnoreCase("logout")){
                    System.out.println("You are logged out");
                    break;
                }

                System.out.println(message);

                if(arr[0].equalsIgnoreCase("download")){
                    String []filename = arr[1].split("/");
                    FileReceiveWithoutAck fileReceiveWithoutAck = new FileReceiveWithoutAck(socket2, filename[3], "noneed", "noneed", "noneed");
                    fileReceiveWithoutAck.start();
                }
            }
            dataInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
