package algorithms.search;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Solution implements Serializable {
    ArrayList<AState> path;

    public Solution() {
        this.path = new ArrayList<AState>();
    }

    public void setPath(ArrayList path) {
        this.path = path;
    }

    public ArrayList<AState> getSolutionPath() {
        return path;
    }
    public boolean isEmpty(){return path.isEmpty();}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        if(solution.path.isEmpty()&&this.path.isEmpty())
            return true;
        return Objects.equals(path, solution.path);
    }


}
