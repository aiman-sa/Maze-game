package algorithms.mazeGenerators;

public class SimpleMazeGenerator extends AMazeGenerator {

    private void directionalSolve(Maze m, int row, int col, int xInc, int yInc)
    {
        if((row == 0 || col == 0 || row == m.getRowLen() -1 ||col == m.getColLen() -1)&&
                (!m.getStartPosition().Equals(row,col))&&(!m.getStartPosition().sameWall(new Position(row,col)))) {
            m.setBoardPos(row,col,0);
            m.getGoalPosition().setPosition(row,col);
            return;
        }
        m.setBoardPos(row,col,0);
        int jump = (int)(Math.random()*2);
        if(jump == 0)
        {
            try
            {
                directionalSolve(m,row + yInc,col,xInc,yInc);
            }catch(Exception e)
            {
                directionalSolve(m,row ,col+xInc,xInc,yInc);
            }
        }
        else
        {
            try
            {
                directionalSolve(m,row ,col+xInc,xInc,yInc);
            }catch(Exception e)
            {
                directionalSolve(m,row+yInc ,col,xInc,yInc);
            }
        }

    }
    private void createSimpleSolution(Maze m)
    {
        int row,col,xInc ,yInc;
        row = m.getStartPosition().getRowIndex();
        col = m.getStartPosition().getColumnIndex();
        if(row <( m.getRowLen())/2 && col <( m.getColLen())/2) {
            xInc = 1;
            yInc = 1;
        }
        else if(row < ( m.getRowLen())/2 && col >=( m.getColLen())/2) {
            xInc = -1;
            yInc = 1;
        }
        else if(row >= ( m.getRowLen())/2 && col >=( m.getColLen())/2) {
            xInc = -1;
            yInc = -1;
        }
        else {
            xInc = 1;
            yInc = -1;
        }
        directionalSolve(m,row,col,xInc,yInc);

    }
    private void createSimpleBoard(Maze m,int row, int col)
    {
    //    m.createBoard(row,col);
        m.fillBoard(1);
        createSimpleSolution(m);
        m.RandomizeBoard();
    }
    public Maze generate(int rows, int cols)
    {
       Maze tmp = new Maze(rows,cols);
       createSimpleBoard(tmp,rows,cols);
       return tmp;
    }
}
