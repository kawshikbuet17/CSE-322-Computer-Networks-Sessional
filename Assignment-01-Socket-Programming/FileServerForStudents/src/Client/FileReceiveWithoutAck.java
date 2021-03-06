package Client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReceiveWithoutAck extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;
    private String privacy;
    private String fileName;
    private String srcPath;
    private String destPath;
    private int chunkSize;

    public FileReceiveWithoutAck(Socket socket, String fileName, String privacy, String srcPath, String destPath, int chunkSize){
        this.socket = socket;
        this.privacy = privacy;
        this.fileName = fileName;
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.chunkSize = chunkSize;
        try{
            dataInputStream = new DataInputStream(socket.getInputStream());
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
            byte[] buffer = new byte[4*1024];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer,0,bytes);
                size -= bytes;      // read upto file size
            }
            fileOutputStream.close();
            if(srcPath.equalsIgnoreCase("noneed")|| destPath.equalsIgnoreCase("noneed")){
                File file = new File("Downloads");
                file.mkdir();
                Files.move(Paths.get(fileName), Paths.get("Downloads/"+fileName));
            }else{
                Files.move(Paths.get(srcPath), Paths.get(destPath));
            }
            System.out.println("Download Complete");
        }catch (Exception e){
            System.out.println("Error in file receiving");
        }
    }
}
