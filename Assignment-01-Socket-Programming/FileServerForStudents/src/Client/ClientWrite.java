package Client;

import java.io.DataOutputStream;
import java.io.File;
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
                dataOutputStream = new DataOutputStream(socket1.getOutputStream());
                System.out.println("1. login");
                System.out.println("2. lookup online users");
                System.out.println("3. request");
                System.out.println("4. upload private");
                System.out.println("5. upload public");
                System.out.println("6. download");
                System.out.println("7. inbox");
                System.out.println("8. viewfiles");
                System.out.println("9. logout");
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                if(message.equalsIgnoreCase("1")){
                    System.out.println("Type your username");
                    String input = scanner.nextLine();
                    message = "login "+input;
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }
                else if(message.equalsIgnoreCase("2")){
                    message = "online";
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }else if(message.equalsIgnoreCase("3")){
                    System.out.println("Request file name?");
                    String input = scanner.nextLine();
                    message = "request "+input;
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }else if(message.equalsIgnoreCase("4")){
                    System.out.println("Upload file name?");
                    String input = scanner.nextLine();
                    message = "uploadf private "+input;
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                    File file = new File(input);
                    dataOutputStream.writeLong(file.length());
                    dataOutputStream.flush();
                }else if(message.equalsIgnoreCase("5")){
                    System.out.println("Upload file name?");
                    String input = scanner.nextLine();
                    message = "uploadf public "+input;
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                    File file = new File(input);
                    dataOutputStream.writeLong(file.length());
                    dataOutputStream.flush();

                }else if(message.equalsIgnoreCase("6")){
                    System.out.println("Download file id?");
                    String input = scanner.nextLine();
                    message = "download "+input;
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }else if(message.equalsIgnoreCase("7")){
                    message = "inbox";
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }else if(message.equalsIgnoreCase("8")){
                    message = "viewfiles";
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }else if(message.equalsIgnoreCase("9")){
                    message = "logout";
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }
            }
        } catch (Exception e) {
            ;
        }
    }
}
