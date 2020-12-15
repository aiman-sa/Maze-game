package algorithms.search;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch extends ASearchingAlgorithm {
    private Queue<AState> queue;

    public BreadthFirstSearch() {
        this.name="BreadthFirstSearch";
        queue=new LinkedList<AState>();
        this.counter=0;
    }

    public Solution solve(ISearchable domain) {
        if(domain==null) {
            return new Solution();
        }
        visitedStates=domain.getVistedStates();
        queue.add(domain.getStartState());
        domain.setIsVisited(visitedStates,domain.getStartState());
        return FinalSolve(domain);
    }

    private Solution FinalSolve(ISearchable domain){
        if(domain==null){
           return new Solution();
        }
        Queue<AState> neighborQ=new LinkedList<>();
        Solution sol=new Solution();
        ArrayList<AState> solution=new ArrayList<>();
        AState minState=null;
        ArrayList<AState> neighbors;
        while(queue.size()>0) {
            minState=minStateInArray(queue);
            //minState=queue.poll();
            if(minState.equals(domain.getGoalState())){
                solution = PathSol(domain,domain.getGoalState());
                sol.setPath(solution);
                return sol;
            }
            neighbors = domain.getAllPossibleStates(minState);
            counter++;
            while (!neighbors.isEmpty()) {
                AState s = neighbors.get(0);
                neighbors.remove(s);
                if (!domain.isSelected(visitedStates, s)) {
                    int x1=s.getStepscounter()+1;
                    int x2=minState.getStepscounter();
                    s.setStepscounter(x1+x2);
                    s.setPrev(minState);
                    domain.setIsVisited(visitedStates,s);
                    queue.add(s);
                }
                else{
                    updateCounterSteps(s,minState);
                }
            }
        }
        solution = PathSol(domain,domain.getGoalState());
        sol.setPath(solution);
        return sol;
    }

    private AState minStateInArray(Queue<AState> q){
        if(q==null){
            return null;
        }
        AState min=q.peek();
        for(AState item : q){
            if(min.getStepscounter()>item.getStepscounter()){
                min=item;
            }
        }
        q.remove(min);
        return min;
    }
}