package FileManagement;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReceiveWithAck extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;
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
            Files.move(Paths.get(srcPath), Paths.get(destPath));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
