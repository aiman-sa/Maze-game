package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    public Maze generate(int rows, int cols) {
        Maze tmp = new Maze(rows,cols);
        //tmp.createBoard(rows,cols);
        return tmp;
    }
}
