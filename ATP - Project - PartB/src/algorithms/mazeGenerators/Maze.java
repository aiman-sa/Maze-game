package algorithms.mazeGenerators;

import java.io.Serializable;

public class Maze implements Serializable {

    private int[][] board;
    private int rowLen,colLen;
    private Position start,goal;

    public Maze(int row, int col) {
        board = new int[row][col];
        rowLen = row;
        colLen = col;
        initPos(row,col);
    }

    private void initPos(int row, int col)
    {
        start = new Position();
        goal = new Position();
        start.RandomizePosition(row,col);
        goal.RandomizePosition(row,col);
        while(start.Equals(goal)||start.sameWall(goal))
            goal.RandomizePosition(row,col);
        int x,y;
        x = start.getRowIndex();
        y = start.getColumnIndex();
        board[x][y] = 0;
        x = goal.getRowIndex();
        y = goal.getColumnIndex();
        board[x][y] = 0;
    }

    public void RandomizeBoard(){
        for(int i = 0 ; i < board.length; i++){
            for(int j = 0 ; j < board[i].length;j++){
                if(board[i][j] == 1)
                    board[i][j] = (int) (Math.random() * 2);
            }
        }
    }
    public void fillBoard(int x){
        for(int i = 0 ; i < board.length; i++){
            for(int j = 0 ; j < board[i].length;j++){
                if(!start.Equals(i,j)&&!goal.Equals(i,j)) {
                    board[i][j] = x;
                }
            }
        }
    }

    public void print(){
        for(int i = 0 ; i <board.length;i++){
            for(int j = 0 ; j < board[i].length;j++){
                if(i == start.getRowIndex() && j == start.getColumnIndex()) {
                    System.out.print("S");
                }
                else if(i == goal.getRowIndex() && j == goal.getColumnIndex()) {
                    System.out.print("E");
                }
                else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
    }
    public Position getStartPosition(){
        return start;
    }
    public Position getGoalPosition(){
        return goal;
    }
    public void setStart(Position start) {
        this.start = start;
    }
    public void setGoal(Position goal) {
        this.goal = goal;
    }

    public int getBoardPos(int x, int y){
        return board[x][y];
    }
    public void setBoardPos(int x, int y, int val){
        board[x][y] = val;
    }
    public int getRowLen(){
        return rowLen;
    }
    public int getColLen(){
        return colLen;
    }
    public int[][] getMaze(){
        return this.board;
    }

    public byte[] toByteArray(){
        byte[] array=new byte[this.getRowLen()*this.getColLen()+12];
        if (array==null){
            return null;
        }
        array[0]=(byte)((this.getRowLen()/255)& 0xFF);
        array[1]=(byte)((this.getRowLen()%255)& 0xFF);
        array[2]=(byte)((this.getColLen()/255)& 0xFF);
        array[3]=(byte)((this.getColLen()%255)& 0xFF);
        array[4]=(byte)((this.getStartPosition().getRowIndex()/255)& 0xFF);
        array[5]=(byte)((this.getStartPosition().getRowIndex()%255)& 0xFF);
        array[6]=(byte)((this.getStartPosition().getColumnIndex()/255)& 0xFF);
        array[7]=(byte)((this.getStartPosition().getColumnIndex()%255)& 0xFF);
        array[8]=(byte)((this.getGoalPosition().getRowIndex()/255)& 0xFF);
        array[9]=(byte)((this.getGoalPosition().getRowIndex()%255)& 0xFF);
        array[10]=(byte)((this.getGoalPosition().getColumnIndex()/255)& 0xFF);
        array[11]=(byte)((this.getGoalPosition().getColumnIndex()%255)& 0xFF);
        int ptr=12;
        int x;
        for(int i=0;i<getMaze().length;i++){
            for(int j=0;j<getMaze()[i].length;j++){
                x=getMaze()[i][j];
                array[ptr]=(byte)(x);
                ptr++;
            }
        }
        return array;
    }

    public Maze(byte[] array) {
        if(array==null || array.length==0){
            return;
        }
        this.rowLen=array[0]*256+array[1];
        this.colLen=array[2]*256+array[3];
        this.start=new Position(array[4]*256+array[5],array[6]*256+array[7]);
        this.goal=new Position(array[8]*256+array[9],array[10]*256+array[11]);
        board = new int[rowLen][colLen];
        int ptr=12;
        for(int i=0;i<rowLen;i++){
            for(int j=0;j<colLen;j++){
                if(ptr<array.length) {
                    board[i][j] = array[ptr] & (0xff);
                    ptr++;
                }
            }
        }
    }
}
