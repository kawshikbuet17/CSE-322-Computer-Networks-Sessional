package FileManagement;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReceiveWithAck extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String privacy;
    private String fileName;
    private String srcPath;
    private String destPath;

    public FileReceiveWithAck(Socket socket, String fileName, String privacy, String srcPath, String destPath){
        this.socket = socket;
        this.privacy = privacy;
        this.fileName = fileName;
        this.srcPath = srcPath;
        this.destPath = destPath;
        try{
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            long size = dataInputStream.readLong();     // read file size
            long totalChunks = dataInputStream.readLong();
            long ack = 0;
            int chunkSize = 4*1024;
            byte[] buffer = new byte[chunkSize];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                dataOutputStream.writeUTF("ack");
                dataOutputStream.flush();
                fileOutputStream.write(buffer,0,bytes);
                size -= bytes;      // read upto file size
                ack++;
            }
            fileOutputStream.close();
            if(totalChunks==ack){
                System.out.println(fileName+" received successfully");
                Files.move(Paths.get(srcPath), Paths.get(destPath));
            }else{
                System.out.println(fileName+" receiving failed");
                File file = new File(srcPath);
                file.delete();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
