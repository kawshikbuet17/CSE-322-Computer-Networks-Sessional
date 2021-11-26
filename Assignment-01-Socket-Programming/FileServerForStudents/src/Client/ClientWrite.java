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
                System.out.println("1. login");
                System.out.println("2. lookup online users");
                System.out.println("3. request");
                System.out.println("4. upload private");
                System.out.println("5. upload public");
                System.out.println("6. download");
                System.out.println("7. inbox");
                System.out.println("8. logout");
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                if(message.equalsIgnoreCase("1")){
                    System.out.println("Type your username");
                    String input = scanner.nextLine();
                    message = "login "+input;
                }
                else if(message.equalsIgnoreCase("2")){
                    message = "online";
                }else if(message.equalsIgnoreCase("3")){
                    System.out.println("Request file name?");
                    String input = scanner.nextLine();
                    message = "request "+input;
                }else if(message.equalsIgnoreCase("4")){
                    System.out.println("Upload file name?");
                    String input = scanner.nextLine();
                    message = "upload "+input+" private";
                }else if(message.equalsIgnoreCase("5")){
                    System.out.println("Upload file name?");
                    String input = scanner.nextLine();
                    message = "upload "+input+" public";

                }else if(message.equalsIgnoreCase("6")){
                    System.out.println("Download file path?");
                    String input = scanner.nextLine();
                    message = "download "+input;
                }else if(message.equalsIgnoreCase("7")){
                    message = "inbox";
                }else if(message.equalsIgnoreCase("8")){
                    message = "logout";
                }
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
