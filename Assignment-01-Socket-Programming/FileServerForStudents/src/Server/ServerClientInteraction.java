package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ServerClientInteraction extends Thread{
    private Socket socket1;
    private Socket socket2;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerClientInteraction(Socket socket1, Socket socket2) {
        this.socket1 = socket1;
        this.socket2 = socket2;
        try {
            dataInputStream = new DataInputStream(socket1.getInputStream());
            dataOutputStream = new DataOutputStream(socket1.getOutputStream());
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


    @Override
    public void run(){
        try{
            String message;
            while (true) {
                if(socket1.isClosed()){
                    break;
                }
                message = dataInputStream.readUTF();
                System.out.println(message);
                String []arr = message.split("\\ ");
                if(arr[0].equalsIgnoreCase("login")){
                    if(!Server.singleUser.contains(arr[1])){
                        User user = new User(arr[1]);
                        Server.socketUserHashMap1.put(socket1, user);
                        Server.socketUserHashMap2.put(socket2, user);
                        System.out.println("username : " + arr[1]);
                        createFolder(arr[1]);
                        Server.singleUser.add(arr[1]);
                    }
                    else{
                        dataOutputStream.writeUTF("User already exists..");
                        dataOutputStream.flush();
                        dataOutputStream.close();
                        socket1.close();
                        socket2.close();
                    }
                }

                if(!arr[0].equalsIgnoreCase("login")){
                    if(!Server.socketUserHashMap1.containsKey(socket1)){
                        dataOutputStream.writeUTF("You are not logged in.");
                        dataOutputStream.flush();
                    }
                    else{
                        if(arr[0].equalsIgnoreCase("upload")){
                            String []tempSplit = message.split(" ", 3);
                            long fileSize = dataInputStream.readLong();
                            int chunkSize = dataInputStream.readInt();
                            if(tempSplit[1].equalsIgnoreCase("private")){
                                String userName = Server.socketUserHashMap1.get(socket1).getUserName();
                                String tempFileName = userName+"_"+tempSplit[2];

                                FileReceiveWithAck fileReceiveWithAck = new FileReceiveWithAck(socket2, tempFileName, "private", tempFileName, "Storage/"+userName+"/private/"+tempFileName, fileSize, chunkSize);
                                fileReceiveWithAck.start();
                            }
                            else{
                                String userName = Server.socketUserHashMap1.get(socket1).getUserName();
                                String tempFileName = userName+"_"+tempSplit[2];
                                FileReceiveWithAck fileReceiveWithAck = new FileReceiveWithAck(socket2, tempFileName, "public", tempFileName, "Storage/"+userName+"/public/"+tempFileName, fileSize, chunkSize);
                                fileReceiveWithAck.start();
                            }
                        }

                        if(arr[0].equalsIgnoreCase("uploadf")){
                            int chunkSize = Server.genChunkSize();
                            long fileSize = dataInputStream.readLong();
                            String []tempSplit = message.split(" ", 3);
                            dataOutputStream.writeUTF("upload "+arr[1]);
                            dataOutputStream.flush();
                            dataOutputStream.writeUTF(tempSplit[2]);
                            dataOutputStream.flush();
                            dataOutputStream.writeLong(fileSize);
                            dataOutputStream.flush();
                            dataOutputStream.writeInt(chunkSize);
                            dataOutputStream.flush();
                        }

                        if(arr[0].equalsIgnoreCase("viewfiles")){
                            FileViewProtocol fileViewProtocol = new FileViewProtocol(socket1);
                            fileViewProtocol.viewFiles(Server.socketUserHashMap1.get(socket1));
                        }

                        if(arr[0].equalsIgnoreCase("download")){
                            String []filename = arr[1].split("/");
                            dataOutputStream.writeUTF(message);
                            dataOutputStream.flush();
                            dataOutputStream.writeInt(Server.MAX_CHUNK_SIZE);
                            dataOutputStream.flush();
                            String []tempSplit = message.split("\\ ", 2);
                            if(filename[2].equalsIgnoreCase("private")){
                                if(filename[1].equalsIgnoreCase(Server.socketUserHashMap1.get(socket1).getUserName())){
                                    FileSendWithoutAck fileSendWithoutAck = new FileSendWithoutAck(socket2, tempSplit[1], "private", Server.MAX_CHUNK_SIZE);
                                    fileSendWithoutAck.start();
                                }else{
                                    dataOutputStream.writeUTF("Access Denied to others' private files");
                                    dataOutputStream.flush();
                                }
                            }
                            else{
                                FileSendWithoutAck fileSendWithoutAck = new FileSendWithoutAck(socket2, tempSplit[1], "public", Server.MAX_CHUNK_SIZE);
                                fileSendWithoutAck.start();
                            }
                        }

                        if(arr[0].equalsIgnoreCase("online")){
                            String onlineUsers = "";
                            for(int i = 0; i<Server.clientSockets1.size(); i++){
                                if (!Server.clientSockets1.get(i).isClosed()){
                                    if(Server.socketUserHashMap1.get(Server.clientSockets1.get(i))!=null){
                                        onlineUsers += Server.socketUserHashMap1.get(Server.clientSockets1.get(i)).getUserName()+"\n";
                                    }
                                }
                            }
                            dataOutputStream.writeUTF(onlineUsers);
                            dataOutputStream.flush();
                        }

                        if(arr[0].equalsIgnoreCase("request")){
                            String userName = Server.socketUserHashMap1.get(socket1).getUserName();
                            String []tempSplit = message.split(" ", 2);
                            for(int i=0; i<Server.clientSockets1.size(); i++){
                                if(!Server.clientSockets1.get(i).isClosed()){
                                    DataOutputStream dataOutputStream1 = new DataOutputStream(Server.clientSockets1.get(i).getOutputStream());
                                    dataOutputStream1.writeUTF(userName+ " requested for file "+tempSplit[1]);
                                    dataOutputStream1.flush();
                                }
                            }
                            if(Server.socketUserHashMap1.get(socket1).getUserName().equalsIgnoreCase(userName)){
                                Request request = new Request(Server.socketUserHashMap1.get(socket1), tempSplit[1]);
                                Server.requests.add(request);
                            }
                        }

                        if(arr[0].equalsIgnoreCase("inbox")){
                            User user = Server.socketUserHashMap1.get(socket1);
                            dataOutputStream.writeUTF(user.checkInbox());
                            dataOutputStream.flush();
                            user.clearInbox();

                        }

                        if(arr[0].equalsIgnoreCase("logout")){
                            dataOutputStream.writeUTF("logout");
                            dataOutputStream.flush();
                            dataOutputStream.close();
                            socket1.close();
                            socket2.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
