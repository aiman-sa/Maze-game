package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import java.io.*;

public class ServerStrategySolveSearchProblem implements IServerStrategy{

    private static int counter;

    public ServerStrategySolveSearchProblem() {
        counter=0;
    }

    public void ServerStrategy(InputStream inputStream, OutputStream outputStream) {
        try {
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");
            String currentDirectoryPath = System.getProperty("user.dir");
            boolean T = false;
            ObjectInputStream fromClient = new ObjectInputStream(inputStream);
            ObjectOutputStream toClient = new ObjectOutputStream(outputStream);
            toClient.flush();
            try {
                Maze maze = ((Maze) fromClient.readObject());
                byte[] mazeToByteArray = maze.toByteArray();
                File dir = new File(tempDirectoryPath);
                File[] tempDirectory = dir.listFiles();
                if (tempDirectory != null) {
                    for (File temp : tempDirectory) {
                        if (!(temp.getName().contains("SolutionNumber"))) {
                            break;
                        }
                        InputStream tempFile = new FileInputStream(temp);
                        ObjectInputStream fromFile = new ObjectInputStream(tempFile);
                        byte[] outputByteArray = ((byte[]) fromFile.readObject());
                        if (mazeToByteArray.equals(outputByteArray)) {
                            T = true;
                            try {
                                String name = temp.getName();
                                File solutionFile = new File(currentDirectoryPath + "\\" + name);
                                InputStream fileSolution = new FileInputStream(solutionFile);
                                ObjectInputStream solution = new ObjectInputStream(fileSolution);
                                Solution FinalSol = (Solution) solution.readObject();
                                toClient.writeObject(FinalSol);
                                tempFile.close();
                                fromFile.close();
                                fileSolution.close();
                                solution.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (!T) {
                    ISearchable searchable = new SearchableMaze(maze);
                    ASearchingAlgorithm search;
                    String s = Server.Configurations.config.getProperty("Searching");
                    if (s.equals("BestFirstSearch")) {
                        search = new BestFirstSearch();
                    } else if (s.equals("BreadthFirstSearch")) {
                        search = new BreadthFirstSearch();
                    } else {
                        search = new DepthFirstSearch();
                    }
                    Solution solution = search.solve(searchable);
                    toClient.writeObject(solution);
                    FileOutputStream fileSolution = new FileOutputStream(currentDirectoryPath + "\\" + "SolutionNumber" + counter);
                    ObjectOutputStream solutionFile = new ObjectOutputStream(fileSolution);
                    solutionFile.writeObject(solution);
                    FileOutputStream fileMaze = new FileOutputStream(tempDirectoryPath + "\\" + "SolutionNumber" + counter);
                    ObjectOutputStream mazeFile = new ObjectOutputStream(fileMaze);
                    mazeFile.writeObject(mazeToByteArray);
                    counter++;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
