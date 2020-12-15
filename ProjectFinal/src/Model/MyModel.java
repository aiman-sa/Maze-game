package Model;

import Client.Client;
import Client.*;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel{

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    private Maze myMaze ;
    private Solution sol;
    private int rowChar;
    private int colChar;
    static int counter;

    public MyModel() {
        myMaze = null;
        sol=null;
        mazeGeneratingServer=new Server(5400,1000,new ServerStrategyGenerateMaze());
        solveSearchProblemServer=new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }
    public void updateCharacterLocation(int direction) {
        if(this.myMaze == null || direction==-1)
            return;
        switch(direction)
        {
            case 1:
                setPlayerPosition(rowChar+1,colChar-1);
                break;
            case 2: //Down
                setPlayerPosition(rowChar+1,colChar);
                break;
            case 3:
                setPlayerPosition(rowChar+1,colChar+1);
                break;
            case 4: //Left
                setPlayerPosition(rowChar,colChar-1);
                break;
            case 6:
                setPlayerPosition(rowChar,colChar+1);
                break;
            case 7:
                setPlayerPosition(rowChar-1,colChar-1);
                break;
            case 8: //Up
                setPlayerPosition(rowChar-1,colChar);
                break;
            case 9:
                setPlayerPosition(rowChar-1,colChar+1);
                break;
        }
        setChanged();
        notifyObservers();
    }

    public void CommunicateWithServer_MazeGenerating(int row, int col){
        threadPool.execute(() -> {
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{row, col};
                            toServer.writeObject(mazeDimensions);
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject();
                            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            byte[] decompressedMaze = new byte[row*col + 12];
                            is.read(decompressedMaze);
                            Maze m = new Maze(decompressedMaze);
                            //maze.print();
                            myMaze=m;
                            rowChar=myMaze.getStartPosition().getRowIndex();
                            colChar=myMaze.getStartPosition().getColumnIndex();
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException var1) {
                var1.printStackTrace();
            }try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers("maze created");
        });
    }

    public void CommunicateWithServer_SolveSearchProblem() {
        if(this.myMaze == null)
            return;
        rowChar=myMaze.getStartPosition().getRowIndex();
        colChar=myMaze.getStartPosition().getColumnIndex();
        threadPool.execute(() -> {
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            toServer.writeObject(myMaze);
                            toServer.flush();
                            sol = (Solution) fromServer.readObject();


                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException var1) {
                var1.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();

            notifyObservers("solution created");
        });
    }

    public void SaveMaze(){
        if(this.myMaze== null)
            return;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File dest = chooser.showSaveDialog(new Stage());
        if(dest==null){
            return;
        }
        counter++;
        try{
            if(myMaze!=null) {
                ObjectOutputStream newFile = new ObjectOutputStream(new FileOutputStream(dest.getPath() + File.separator));
                newFile.writeObject(myMaze);
                newFile.close();
            }
        }
        catch (IOException e){
      //      e.printStackTrace();
        }

    }
    public void openMaze(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        if(file==null){
            return;
        }
        try{
            ObjectInputStream newFile = new ObjectInputStream(new FileInputStream(file));
            myMaze=(Maze)newFile.readObject();
            colChar=this.myMaze.getStartPosition().getRowIndex();
            rowChar=this.myMaze.getStartPosition().getColumnIndex();
            newFile.close();
            setChanged();
            notifyObservers("file loaded");
        }
        catch (IOException e){
         //   e.printStackTrace();
        } catch (ClassNotFoundException e) {
          //  e.printStackTrace();
        }
    }

    public void close(){
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }
    public void setPlayerPosition(int row_player,int col_player) {
        if(row_player>myMaze.getRowLen()-1 || col_player>myMaze.getColLen()-1 || col_player<0 || row_player<0){

        }
        else if(myMaze.getMaze()[row_player][col_player]!=1) {
            this.rowChar = row_player;
            this.colChar = col_player;
            setChanged();
            if (row_player == myMaze.getGoalPosition().getRowIndex() && col_player == myMaze.getGoalPosition().getColumnIndex()){
                notifyObservers("succeeded");
            }
            else {
                notifyObservers("position updated");
            }
        }
        setChanged();
    }
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    public Solution getSolution() {
        return this.sol;
    }
    public void setMaze(Maze myMaze) { this.myMaze = myMaze; }
    public Maze getMaze() {
        return myMaze;
    }
    public void setSolution(Solution sol) { this.sol=sol; }
    public int getRowChar() {
        return rowChar;
    }
    public int getColChar() {
        return colChar;
    }
}
