package algorithms.search;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class BestFirstSearch extends ASearchingAlgorithm{
    private PriorityQueue<AState> PQueue;

    public BestFirstSearch() {
        this.name="BestFirstSearch";
        this.counter=0;
        PQueue=new PriorityQueue<AState>(this::compareFunc);
    }

    private int compareFunc(AState s1, AState s2){
        if(s1.getCost()>s2.getCost()){ return 1; }
        if(s1.getCost()==s2.getCost()){return 0;}
        else{return -1;}
    }
    public Solution solve(ISearchable domain) {
        if(domain==null) {
            return new Solution();
        }
        visitedStates=domain.getVistedStates();
        PQueue.add(domain.getStartState());
        domain.setIsVisited(visitedStates,domain.getStartState());
        return FinalSolve(domain);
    }

    private Solution FinalSolve(ISearchable domain){
        if(domain==null){
            return new Solution();
        }
        PriorityQueue<AState> neighborQ=new PriorityQueue<>();
        Solution sol=new Solution();
        ArrayList<AState> solution=new ArrayList<>();
        AState minState=null;
        ArrayList<AState> neighbors;
        while(PQueue.size()>0) {
            minState=minStateInArray(this.PQueue);
            //minState=PQueue.poll();
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
                    double x1=s.getCost();
                    double x2=minState.getCost();
                    s.setCost(x1+x2);
                    s.setPrev(minState);
                    domain.setIsVisited(visitedStates,s);
                    PQueue.add(s);
                }
                else{
                    updateCost(s,minState);
                }
            }
        }
        solution = PathSol(domain,domain.getGoalState());
        sol.setPath(solution);
        return sol;
    }

    private AState minStateInArray(PriorityQueue<AState> q){
        if(q==null){
            return null;
        }
        AState min=q.peek();
        for(AState item : q){
            if(min.getCost()>item.getCost()){
                min=item;
            }
        }
        q.remove(min);
        return min;
    }
}
