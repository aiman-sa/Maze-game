package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator{

    public abstract Maze generate(int rows, int columns);

    public long measureAlgorithmTimeMillis(int row,int col){
        long StartTime = System.currentTimeMillis();
        generate(row, col);
        long StopTime = System.currentTimeMillis();
        return StopTime - StartTime;
    }
}
