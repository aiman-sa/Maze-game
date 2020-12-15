package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy{

    public void ServerStrategy(InputStream inputStream, OutputStream outputStream) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inputStream);
            ObjectOutputStream toClient = new ObjectOutputStream(outputStream);
            toClient.flush();
            int[] sizeOfMaze = new int[2];
            try {
                sizeOfMaze = (int[]) fromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            AMazeGenerator generateMaze;
            String s = Server.Configurations.config.getProperty("MazeGenerator");
            if (s.equals("MyMazeGenerator")) {
                generateMaze = new MyMazeGenerator();
            } else {
                generateMaze = new SimpleMazeGenerator();
            }
            Maze maze = generateMaze.generate(sizeOfMaze[0], sizeOfMaze[1]);
            byte[] mazeToByteArray = maze.toByteArray();
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            byteOutput.flush();
            MyCompressorOutputStream compressByteArrayMaze = new MyCompressorOutputStream(byteOutput);
            compressByteArrayMaze.write(mazeToByteArray);
            toClient.writeObject(byteOutput.toByteArray());
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
