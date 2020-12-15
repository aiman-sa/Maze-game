package Client;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    InetAddress IP;
    int port;
    IClientStrategy clientStrategy;

    public Client(InetAddress IP, int port, IClientStrategy clientStrategy) {
        this.IP = IP;
        this.port = port;
        this.clientStrategy = clientStrategy;
    }

    public void communicateWithServer() {
        try {
            Socket theServer = new Socket(IP, port);
            System.out.println("Connected to server!");
            clientStrategy.clientStrategy(theServer.getInputStream(),theServer.getOutputStream());
            theServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

