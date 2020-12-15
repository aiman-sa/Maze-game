package JUnit;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.BestFirstSearch;
import algorithms.search.SearchableMaze;
import org.junit.jupiter.api.Assertions;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JUnitTestingBestFirstSearch {
    @org.junit.jupiter.api.Test
    void TestName()
    {
        assertEquals("BestFirstSearch", new BestFirstSearch().getName());
    }

    @org.junit.jupiter.api.Test
    void PositiveEvaluatedNumber()
    {
        BestFirstSearch BFS = new BestFirstSearch();
        MyMazeGenerator myMaze = new MyMazeGenerator();
        Maze maze = myMaze.generate(100, 100);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        BFS.solve(searchableMaze);
        Assertions.assertTrue(BFS.getNumberOfNodesEvaluated() > 0);
    }
    @org.junit.jupiter.api.Test
    void GenerateInMinute()
    {
        MyMazeGenerator myMaze = new MyMazeGenerator();
        long before = System.currentTimeMillis();
        Maze maze = myMaze.generate(1000, 1000);
        long after = System.currentTimeMillis() ;
        Assertions.assertTrue(after-before<=60000);
    }

    @org.junit.jupiter.api.Test
    void DifferentStartAndGoal()
    {
        IMazeGenerator gen = new MyMazeGenerator();
        Maze maze = gen.generate(10, 10);
        Position start=maze.getStartPosition();
        Position goal=maze.getGoalPosition();
        Assertions.assertTrue(goal!=start);
    }
}