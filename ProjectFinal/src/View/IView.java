package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IView {

    void generateMaze();
    void solveMaze() ;
    void SaveMaze();
    void openMaze();
}
