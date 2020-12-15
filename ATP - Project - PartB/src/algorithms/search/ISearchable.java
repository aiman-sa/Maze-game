package algorithms.search;

import java.util.ArrayList;

public interface ISearchable {
    AState getStartState();
    AState getGoalState();
    ArrayList<AState> getAllPossibleStates(AState s);
    boolean[][] getVistedStates();
    //AState[][] getMatrixStates();
    boolean isSelected(boolean[][] arrays,AState s);
    void setIsVisited(boolean[][] arrays,AState s);
    int compareStates(AState s1,AState s2);
}
