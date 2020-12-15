package test;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Aviadjo on 3/26/2017.
 */
public class RunCompressDecompressMaze {
    public static void main(String[] args) {
        String mazeFileName = "savedMaze.maze";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
        Maze maze = mazeGenerator.generate(10, 10); //Generate new maze
        System.out.println(maze.getStartPosition().toString());
        System.out.println(maze.getGoalPosition().toString());
        System.out.println(maze.getColLen());
        System.out.println(maze.getRowLen());
        maze.print();
        try {
            // save maze to a file
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte savedMazeBytes[] = new byte[0];
        try {
            //read maze from file
            InputStream in = new MyDecompressorInputStream(new FileInputStream(mazeFileName));
            savedMazeBytes = new byte[maze.toByteArray().length];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Maze loadedMaze = new Maze(savedMazeBytes);
        System.out.println(loadedMaze.getStartPosition().toString());
        System.out.println(loadedMaze.getGoalPosition().toString());
        System.out.println(loadedMaze.getColLen());
        System.out.println(loadedMaze.getRowLen());
        loadedMaze.print();
        boolean areMazesEquals = Arrays.equals(loadedMaze.toByteArray(),maze.toByteArray());
        System.out.println(String.format("Mazes equal: %s",areMazesEquals)); //maze should be equal to loadedMaze
    }
}