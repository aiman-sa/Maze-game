package Model;

import java.util.Observer;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IModel {
   // public void generateRandomMaze(int row, int col);
    void CommunicateWithServer_MazeGenerating(int row, int col);
    void CommunicateWithServer_SolveSearchProblem();
    Maze getMaze();
    void updateCharacterLocation(int direction);
    int getRowChar();
    int getColChar();
    void assignObserver(Observer o);
  //  void solveMaze(Maze maze);
    public Solution getSolution();
    void close();
    void SaveMaze();
    void openMaze();
    void setSolution(Solution sol);

}
