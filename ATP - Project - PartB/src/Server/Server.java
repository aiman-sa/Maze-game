package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private int port;//The port
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private ExecutorService threadPoolExecutor;
    private InputStream input = null;
    private Properties proper = new Properties();

    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        Configurations.RunProp();
        try {
            input = new FileInputStream("config.properties");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            proper.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.port = port;
        this.serverStrategy = serverStrategy;
        this.listeningInterval = listeningInterval;
        this.threadPoolExecutor = Executors.newFixedThreadPool(Integer.parseInt(proper.getProperty("NumberOfThreads")));
        this.stop = false;
    }
    public void start() {
        new Thread(() -> {
            runServer();
        }).start();
    }
    public void runServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);
            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    this.threadPoolExecutor.execute(() ->{
                        clientHandle(clientSocket);
                    });
                }
                catch (IOException e) {
      //              System.out.println("Where are the clients??");
                }
            }
            serverSocket.close();
            //threadPoolExecutor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clientHandle(Socket clientSocket) {
        try {
            serverStrategy.ServerStrategy(clientSocket.getInputStream(),clientSocket.getOutputStream());
            clientSocket.getInputStream().close();
            clientSocket.getOutputStream().close();
            clientSocket.close();
        } catch (IOException e) {
        //    e.printStackTrace();
        }
    }
    public void stop()
    {
        System.out.println("The server has stopped!");
        this.stop = true;
        this.threadPoolExecutor.shutdown();
    }

    public static class Configurations{
        public static Properties config = new Properties();
        public static OutputStream output = null;
        public static void RunProp()  {
            try {
                output = new FileOutputStream("config.properties");
                config.setProperty("NumberOfThreads", "10");
                config.setProperty("Searching", "BestFirstSearch");
                config.setProperty("MazeGenerator", "MyMazeGenerator");
                config.setProperty("Searching","BestFirstSearch");
                try {
                    config.store(output, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}