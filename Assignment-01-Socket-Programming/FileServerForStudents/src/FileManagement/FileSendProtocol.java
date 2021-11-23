package FileManagement;

import java.io.*;
import java.net.Socket;

public class FileSendProtocol {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private String privacy;

    public FileSendProtocol(Socket socket, String privacy) throws IOException {
        this.socket = socket;
        this.privacy = privacy;
        try{
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        dataOutputStream.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }
}
