package algorithms.search;

import algorithms.mazeGenerators.Maze;
import java.util.ArrayList;

public class SearchableMaze implements ISearchable {

    private Maze myMaze;
    private MazeState start;
    private MazeState goal;
    private MazeState[][] states;
    private boolean[][] visitedStates;

    public SearchableMaze(Maze maze) {
        if(maze!=null) {
            this.myMaze = maze;
            start = new MazeState(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
            goal = new MazeState(maze.getGoalPosition().getRowIndex(), maze.getGoalPosition().getColumnIndex());
            visitedStates=new boolean[maze.getMaze().length][maze.getMaze()[0].length];
            states=new MazeState[maze.getMaze().length][maze.getMaze()[0].length];
            for(int i=0;i<states.length;i++) {
                for (int j = 0; j < states[i].length; j++) {
                    states[i][j] = new MazeState(i, j);
                }
            }
            states[start.getRow()][start.getCol()]=start;
            states[goal.getRow()][goal.getCol()]=goal;
        }
    }

    public AState getStartState() {
        return this.start;
    }

    public AState getGoalState() {
        return this.goal;
    }

    public ArrayList<AState> getAllPossibleStates(AState state) {
        if(state==null || !(state instanceof MazeState)){
            return null;
        }
        ArrayList<AState> array;
        MazeState mazeState=(MazeState)state;
        array=addNeighbor(mazeState);
        return array;
    }

    public ArrayList addNeighbor(MazeState mazeState){
        if(mazeState==null){
            return null;
        }
        ArrayList<AState> array=new ArrayList<>();
        if(mazeState!=null) {
            if(isPossible(array,mazeState.getRow() + 1,mazeState.getCol())){
                array.add(states[mazeState.getRow() + 1][mazeState.getCol()]);
                array=diagonalNeighbor(array,mazeState.getRow()+1,mazeState.getCol()+1);
                array=diagonalNeighbor(array,mazeState.getRow()+1,mazeState.getCol()-1);
            }
            if(isPossible(array,mazeState.getRow()-1,mazeState.getCol())){
                array.add(states[mazeState.getRow()-1][mazeState.getCol()]);
                array=diagonalNeighbor(array,mazeState.getRow()-1,mazeState.getCol()+1);
                array=diagonalNeighbor(array,mazeState.getRow()-1,mazeState.getCol()-1);
            }
            if(isPossible(array,mazeState.getRow(),mazeState.getCol() + 1)){
                array.add(states[mazeState.getRow()][mazeState.getCol() + 1]);
                array=diagonalNeighbor(array,mazeState.getRow()-1,mazeState.getCol()+1);
                array=diagonalNeighbor(array,mazeState.getRow()+1,mazeState.getCol()+1);
            }
            if(isPossible(array,mazeState.getRow(),mazeState.getCol()-1)){
                array.add(states[mazeState.getRow()][mazeState.getCol()-1]);
                array=diagonalNeighbor(array,mazeState.getRow()-1,mazeState.getCol()-1);
                array=diagonalNeighbor(array,mazeState.getRow()+1,mazeState.getCol()-1);
            }
        }
        return array;
    }

    private ArrayList diagonalNeighbor(ArrayList array,int row,int col){
        if (row<0 || col<0 || row >= myMaze.getMaze().length || col >= myMaze.getMaze()[0].length) {
            return array;
        }
        MazeState state=this.states[row][col];
        if(myMaze.getBoardPos(state.getRow(),state.getCol())==0 && !array.contains(state) ){//&& !visitedStates[row][col]
            if(state.getCost()==0){
                state.setCost(15);
            }
            visitedStates[row][col]=true;

            array.add(state);
        }
        return array;
    }

    private boolean isPossible(ArrayList array,int row,int col) {
        if (row<0 || col<0 || row>= myMaze.getMaze().length || col >= myMaze.getMaze()[0].length) {
            return false;
        }
        MazeState state=this.states[row][col];
        if (myMaze.getBoardPos(state.getRow(), state.getCol()) == 0 && !array.contains(state) ) {//&& !visitedStates[row][col]
            if(state.getCost()==0) {
                state.setCost(10);
            }
            visitedStates[row][col]=true;
            return true;
        }
        return false;
    }
    public MazeState[][] getMatrixStates() {
        return states;
    }

    public boolean isSelected(boolean[][] arrays,AState state) {
        if(state!=null && state instanceof MazeState && arrays!=null){
            MazeState s=(MazeState)state;
            return arrays[s.getRow()][s.getCol()];
        }
        return false;
    }

    public void setIsVisited(boolean[][] arrays,AState state) {
        if(state!=null && state instanceof MazeState) {
            MazeState s = (MazeState) state;
            arrays[s.getRow()][s.getCol()] = true;
        }
    }

    public int compareStates(AState s1, AState s2) {
        if(s1!=null && s2!=null && s1 instanceof MazeState && s2 instanceof MazeState){
            MazeState m1=(MazeState)s1;
            MazeState m2=(MazeState)s1;
            if(m1.getRow()>m2.getRow()){
                return 1;
            }
        }
        return 0;
    }

    public boolean[][] getVistedStates() {
        return new boolean[states.length][states[0].length];
    }

}
