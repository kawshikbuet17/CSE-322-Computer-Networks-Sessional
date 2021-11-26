package Server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileSendWithoutAck extends Thread{
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private String privacy;
    private String path;

    public FileSendWithoutAck(Socket socket, String path, String privacy) throws IOException {
        this.socket = socket;
        this.path = path;
        this.privacy = privacy;
        try{
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
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
            byte[] buffer = new byte[4*1024];
            while ((bytes=fileInputStream.read(buffer))!=-1){
                dataOutputStream.write(buffer,0,bytes);
                dataOutputStream.flush();
            }
            fileInputStream.close();
            System.out.println(path+" Download Procedure Completed");
        }catch (Exception e){
            System.out.println("Error in file sending");
        }
    }
}
