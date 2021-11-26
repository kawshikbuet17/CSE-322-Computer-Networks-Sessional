package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientRead extends Thread{
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket1;
    private Socket socket2;

    public ClientRead(Socket socket1, Socket socket2) {
        this.socket1 = socket1;
        this.socket2 = socket2;
        try{
            dataInputStream = new DataInputStream(socket1.getInputStream());
            dataOutputStream = new DataOutputStream(socket1.getOutputStream());

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            String message;
            while (true) {
                if(socket1.isClosed() || socket2.isClosed()){
                    break;
                }
                message = dataInputStream.readUTF();
                String []arr = message.split("\\ ", 2);
                if(message.equalsIgnoreCase("logout")){
                    System.out.println("You are logged out");
                    break;
                }

                System.out.println(message);

                if(arr[0].equalsIgnoreCase("download")){
                    int chunkSize = dataInputStream.readInt();
                    String []filename = arr[1].split("/");
                    FileReceiveWithoutAck fileReceiveWithoutAck = new FileReceiveWithoutAck(socket2, filename[3], "noneed", "noneed", "noneed", chunkSize);
                    fileReceiveWithoutAck.start();
                }

                if(arr[0].equalsIgnoreCase("upload")){
                    System.out.println("uploading ...");
                    String filePath = dataInputStream.readUTF();
                    long fileSize = dataInputStream.readLong();
                    int chunkSize = dataInputStream.readInt();
                    dataOutputStream.writeUTF("upload "+arr[1]+" "+filePath);
                    dataOutputStream.flush();
                    dataOutputStream.writeLong(fileSize);
                    dataOutputStream.flush();
                    dataOutputStream.writeInt(chunkSize);
                    dataOutputStream.flush();

                    FileSendWithAck fileSendWithAck = new FileSendWithAck(socket2, filePath, arr[1], fileSize, chunkSize);
                    fileSendWithAck.start();
                }
            }
            dataInputStream.close();
        }catch (Exception e){
            ;
        }
    }
}
