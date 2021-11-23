package FileManagement;

import java.net.Socket;

public class ClientDownload{
    private Socket socket;

    public ClientDownload(Socket socket) {
        this.socket = socket;
    }

    public void downloadFile(String filename) throws Exception{
        FileReceiveProtocol fileReceiveProtocol = new FileReceiveProtocol(socket, "noneed");
        fileReceiveProtocol.receiveFile(filename);
    }
}
