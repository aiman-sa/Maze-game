package algorithms.mazeGenerators;

import java.util.Stack;

public class MyMazeGenerator extends AMazeGenerator {
    private boolean isValid(Maze m,Position pos){
        if(m==null || pos==null){
            return false;
        }
        int row = pos.getRowIndex();
        int col = pos.getColumnIndex();
        if(row < 0 || row >= m.getRowLen() || col < 0 || col >= m.getColLen())
            return false;
        return true;
    }
    private boolean isVisited(Position pos, int[][] visited){
        if(pos==null){
            return false;
        }
        int row = pos.getRowIndex();
        int col = pos.getColumnIndex();
        return visited[row][col]==1;
    }
    private Position pickPosition(Maze m,Position[] posArr, int idx,int [][]visited){
        if(m==null || posArr.length==0 || idx>posArr.length || idx<0){
            return null;
        }
        if(isValid(m,posArr[idx])&&!isVisited(posArr[idx],visited))
            return posArr[idx];
        return null;
    }
    private Position[] initNeighbours(Position pos){
        if(pos==null){
            return null;
        }
        Position[] posArr = new Position[4];
        int row = pos.getRowIndex();
        int col = pos.getColumnIndex();
        posArr[0] = new Position(row-1,col);
        posArr[1] = new Position(row+1,col);
        posArr[2] = new Position(row,col-1);
        posArr[3] = new Position(row,col+1);
        return posArr;
    }
    private Position[] initSecondNeighbours(Position pos){
        if(pos==null){
            return null;
        }
        Position[] posArr = new Position[4];
        int row = pos.getRowIndex();
        int col = pos.getColumnIndex();
        posArr[0] = new Position(row-2,col);
        posArr[1] = new Position(row+2,col);
        posArr[2] = new Position(row,col-2);
        posArr[3] = new Position(row,col+2);

        return posArr;
    }
    private Position randomChoose(Maze m,Stack<Position> mem,Position pos,int[][]visited){
        if(m==null || pos==null){
            return null;
        }
        Position[] posArr = initNeighbours(pos);
        Position[] posArrSecond =  initSecondNeighbours(pos);
        int counter = 0;
        for(int i = 0 ; i < 4 ; i++){
            posArr[i] = pickPosition(m,posArr,i,visited);
            if(posArr[i] == null)
                counter++;
        }
        int counter2  = 0;
        for(int i = 0 ; i < 4 ; i++){
            Position temp = posArrSecond[i];
            if(posArr[i] != null&&(isValid(m,temp)&&isVisited(temp,visited)))
                counter2++;
        }
        if(counter == 4)
            return null;
        else if(4 - counter == counter2)
            return null;
        boolean check = false;
        while(!check){
            int idx = (int)(Math.random()*4);
            if(posArr[idx]!=null){
                Position toCheck = posArrSecond[idx];
                if(!isValid(m,toCheck)||!isVisited(toCheck,visited)){
                    return posArr[idx];
                }
            }
        }
        return null;
    }
    private void DFSMaze(Maze m,Stack<Position> mem,int[][] visited){
        if(m==null){
            return;
        }
        mem.push(m.getStartPosition());
        visited[m.getStartPosition().getRowIndex()][m.getStartPosition().getColumnIndex()] = 1;
        while(!mem.empty()){
            Position curr = mem.pop();
            Position jump = randomChoose(m,mem,curr,visited);
            if(jump!=null){
                mem.push(curr);
                int row = jump.getRowIndex();
                int col = jump.getColumnIndex();
                m.setBoardPos(row,col,0);
                visited[row][col] = 1;
                int currRow = curr.getRowIndex();
                int currCol = curr.getColumnIndex();
                int jumpRow = currRow+((row-currRow)*2);
                int jumpCol = currCol+((col-currCol)*2);
                Position toCheck = new Position(jumpRow,jumpCol);
                if(isValid(m,toCheck)){
                    visited[jumpRow][jumpCol] = 1;
                    m.setBoardPos(jumpRow,jumpCol,0);
                    mem.push(toCheck);
                }
            }
        }
    }
    public Maze generate(int rows, int cols){
        Maze M = new Maze(rows,cols);
       // M.createBoard(rows,cols);
        M.fillBoard(1);
        DFSMaze(M,new Stack<Position>(),new int[rows][cols]);
      //  M.print();
        return M;
    }
}