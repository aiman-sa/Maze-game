package algorithms.search;

import java.io.Serializable;

public class MazeState extends AState implements Serializable {
    private int row;
    private int col;
    private boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public MazeState(int row, int col) {
        super();
        this.row=row;
        this.col=col;
    }

    public String toString() {
        String str=row + "," + col;
        return str;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
