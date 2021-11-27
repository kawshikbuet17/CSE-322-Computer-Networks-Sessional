package Server;

import Server.Server;

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
    private long fileSize;
    private int chunkSize;

    public FileReceiveWithAck(Socket socket, String fileName, String privacy, String srcPath, String destPath, long fileSize, int chunkSize){
        this.socket = socket;
        this.privacy = privacy;
        this.fileName = fileName;
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.chunkSize = chunkSize;
        this.fileSize = fileSize;
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
                String []arr = fileName.split("_");

                int temp = -1;
                for(int i =0; i< Server.requests.size(); i++){
                    if(Server.requests.get(i).getFilename().equalsIgnoreCase(arr[1])){
                        for(int j=0; j<Server.clientSockets2.size(); j++){
                            Socket socket = Server.clientSockets2.get(j);
                            if(!socket.isClosed()){
                                if(Server.socketUserHashMap2.get(socket).getUserName().equalsIgnoreCase(Server.requests.get(i).getUser().getUserName())){
                                    Server.requests.get(i).getUser().addMessage(arr[0]+ " has uploaded file "+arr[1]);
                                    System.out.println(arr[0]+" has uploaded file " + arr[1]);
                                }
                            }
                        }
                    }
                }
            }else{
                System.out.println(fileName+" receiving failed");
                File file = new File(srcPath);
                file.delete();
            }
            Server.AVAILABLE_BUFFER_SIZE += fileSize;

        }catch (Exception e){
            System.out.println("Error in file receiving");
            Server.AVAILABLE_BUFFER_SIZE = (int) Math.min(Server.AVAILABLE_BUFFER_SIZE+fileSize, Server.MAX_BUFFER_SIZE);
        }
    }
}
