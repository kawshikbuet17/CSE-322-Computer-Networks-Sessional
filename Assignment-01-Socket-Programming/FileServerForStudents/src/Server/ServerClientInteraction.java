package Server;

import FileManagement.FileReceiveProtocol;
import FileManagement.FileSendProtocol;
import FileManagement.FileViewProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClientInteraction extends Thread{
    private Socket socket;
    private Socket socketForDownload;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerClientInteraction(Socket socket, Socket socketForDownload) {
        this.socket = socket;
        this.socketForDownload = socketForDownload;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void createFolder(String folderName){
        File file = new File("Storage");
        file.mkdir();
        file = new File("Storage/"+folderName);
        file.mkdir();
        file = new File("Storage/"+folderName+"/public");
        file.mkdir();
        file = new File("Storage/"+folderName+"/private");
        file.mkdir();
    }

    public void downloadApproval(String path){

    }

    @Override
    public void run(){
        try{
            String message;
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println(message);
                String []arr = message.split("\\ ");
                if(arr[0].equalsIgnoreCase("login")){
                    User user = new User();
                    user.addUserName(arr[1]);
                    Server.socketUserHashMap.put(socket, user);
                    Server.socketUserHashMapForDownload.put(socketForDownload, user);
                    System.out.println("username : " + arr[1]);
                    createFolder(arr[1]);
                }

                if(arr[0].equalsIgnoreCase("upload")){
                    if(arr[2].equalsIgnoreCase("private")){
                        FileReceiveProtocol fileReceiveProtocol = new FileReceiveProtocol(socket, "private");
                        String userName = Server.socketUserHashMap.get(socket).username;
                        String tempFileName = userName+"_"+arr[1];
                        fileReceiveProtocol.receiveFile(tempFileName);
                        fileReceiveProtocol.renameAndMove(tempFileName, "Storage/"+userName+"/private/"+tempFileName);
                    }
                    else{
                        FileReceiveProtocol fileReceiveProtocol = new FileReceiveProtocol(socket, "public");
                        String userName = Server.socketUserHashMap.get(socket).username;
                        String tempFileName = userName+"_"+arr[1];
                        fileReceiveProtocol.receiveFile(tempFileName);
                        fileReceiveProtocol.renameAndMove(tempFileName, "Storage/"+userName+"/public/"+tempFileName);
                    }
                }

                if(arr[0].equalsIgnoreCase("viewfiles")){
                    FileViewProtocol fileViewProtocol = new FileViewProtocol(socket);
                    fileViewProtocol.viewFiles(Server.socketUserHashMap.get(socket));
                }

                if(arr[0].equalsIgnoreCase("download")){
                    String []filename = arr[1].split("/");
                    if(filename[2].equalsIgnoreCase("private")){
                        if(filename[1].equalsIgnoreCase(Server.socketUserHashMap.get(socket).getName())){
                            FileSendProtocol fileSendProtocol = new FileSendProtocol(socketForDownload, "private");
                            fileSendProtocol.sendFile(arr[1]);
                        }
                    }
                    else{
                        FileSendProtocol fileSendProtocol = new FileSendProtocol(socketForDownload, "public");
                        fileSendProtocol.sendFile(arr[1]);
                    }
                }

                if(arr[0].equalsIgnoreCase("online")){
                    String onlineUsers = "";
                    for(int i=0; i<Server.clientSockets.size(); i++){
                        if (!Server.clientSockets.get(i).isClosed()){
                            onlineUsers += Server.socketUserHashMap.get(Server.clientSockets.get(i)).getName()+"\n";
                        }
                    }
                    dataOutputStream.writeUTF(onlineUsers);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
