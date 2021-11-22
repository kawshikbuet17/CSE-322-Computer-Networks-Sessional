package FileSendReceive;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReceiveProtocol {
    private Socket socket;
    private DataInputStream dataInputStream;
    private String privacy;

    public FileReceiveProtocol(Socket socket, String privacy) throws IOException {
        this.socket = socket;
        this.privacy = privacy;
        try{
            dataInputStream = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = dataInputStream.readLong();     // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
    }

    public void renameAndMove(String srcPath, String destPath) throws Exception{
        Files.move(Paths.get(srcPath), Paths.get(destPath));
    }
}
