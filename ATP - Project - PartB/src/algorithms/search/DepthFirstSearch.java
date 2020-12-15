package algorithms.search;

import java.util.*;

public class DepthFirstSearch extends ASearchingAlgorithm {
    Stack<AState> stack;
    List<AState> list=new ArrayList<>();

    public DepthFirstSearch() {
        this.name="DepthFirstSearch";
        this.counter=0;
        stack=new Stack<>();
    }

    public Solution solve(ISearchable domain) {
        if(domain==null) {
            return null;
        }
        visitedStates=domain.getVistedStates();
        DFSUtil(domain,domain.getStartState());
        Solution sol=new Solution();
        ArrayList<AState> solution=new ArrayList<>();
        solution = PathSol(domain,domain.getGoalState());
        sol.setPath(solution);
        return sol;
    }
    private void DFSUtil(ISearchable domain,AState state) {
        if(domain==null || state==null){
            return;
        }
        Stack<AState> stack = new Stack<>();
        stack.push(state);
        while (!stack.empty()) {
            state=stack.lastElement();
            stack.remove(state);
            if(state.equals(domain.getGoalState())){
                return;
            }
            if (!domain.isSelected(visitedStates, state)) {
                domain.setIsVisited(visitedStates, state);
            }
            List<AState> neighbors = domain.getAllPossibleStates(state);
            counter++;
            Iterator<AState> itr = neighbors.iterator();

            while (itr.hasNext()) {
                AState v = itr.next();
                if (!domain.isSelected(visitedStates, v)) {
                    v.setPrev(state);
                    v.setStepscounter(state.getStepscounter()+1);
                    domain.setIsVisited(visitedStates,v);
                    stack.push(v);
                }
                else{
                    updateCounterSteps(v,state);
                }
                if(v.equals(domain.getGoalState())){
                    return;
                }
            }
        }
    }
}

