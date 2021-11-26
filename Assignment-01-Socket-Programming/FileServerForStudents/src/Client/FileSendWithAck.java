package Client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

public class FileSendWithAck extends Thread{
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String privacy;
    private String path;
    private long fileSize;
    private int chunkSize;


    public FileSendWithAck(Socket socket, String path, String privacy, long fileSize, int chunkSize) throws IOException {
        this.socket = socket;
        this.path = path;
        this.privacy = privacy;
        this.chunkSize = chunkSize;
        this.fileSize = fileSize;
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run(){
        try{
            int bytes = 0;
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);

            // send file size
            dataOutputStream.writeLong(file.length());
            dataOutputStream.flush();
            // break file into chunks
            long size = file.length();
            long totalChunks = size/chunkSize;
            long ack = 0;
            if(size%chunkSize != 0){
                totalChunks++;
            }
            dataOutputStream.writeLong(totalChunks);
            dataOutputStream.flush();
            byte[] buffer = new byte[chunkSize];
            while ((bytes=fileInputStream.read(buffer))!=-1){
                if(ack == 0){
                    ack++;
                }else{
                    socket.setSoTimeout(30000);
                    try{
                        if(dataInputStream.readUTF().equalsIgnoreCase("ack")){
                            ack++;
                        }else {
                            System.out.println("chunk not delivered");
                            break;
                        }
                    }catch (Exception e){
                        System.out.println("Time out while receiving acknowledgement");
                    }
                }
                if(ack==totalChunks){
                    System.out.println("Uploaded 100%");
                }
                dataOutputStream.write(buffer,0,bytes);
                dataOutputStream.flush();
            }
            fileInputStream.close();
        }catch (Exception e){
            System.out.println("Error in file sending");
        }
    }
}
