package algorithms.mazeGenerators;
import java.io.Serializable;
public class Position implements Serializable{
    private int row,col;
    public Position(){
        row = 0;
        col = 0;
    }

    public Position(int _row,int _col){
        row = _row;
        col = _col;
    }

    public void  RandomizePosition(int _row,int _col){
        int x, y;
        int wall = ((int) (Math.random() * 4) + 1);// random number between 1-4
        switch (wall) {
            case 1://left wall
                col = 0;
                row = ((int) (Math.random() * _row));
                break;
            case 2://top wall
                row = 0;
                col = ((int) (Math.random() * _col));
                break;
            case 3://right wall
                col = _col - 1;
                row = ((int) (Math.random() * _row));
                break;
            case 4://bottom wall
                row = _row - 1;
                col = ((int) (Math.random() * _col));
                break;
        }
    }

    public void setPosition(int _row, int _col){
        row = _row;
        col = _col;
    }

    public boolean sameWall(Position other){
        if(other==null){
            return false;
        }
        if(row != other.getRowIndex() && col == other.getColumnIndex())
            return true;
        if(col != other.getColumnIndex() && row == other.getRowIndex())
            return true;
        return false;
    }

    public boolean Equals(Position other){
        if(other==null){
            return false;
        }
        return (row == other.getRowIndex()&& col == other.getColumnIndex());
    }

    public boolean Equals(int i, int j){
        return (row == i&& col == j);
    }

    public int getRowIndex(){return row;}
    public int getColumnIndex(){return col;}

    public String toString(){
        return "{" + row + "," + col + "}";
    }
}


