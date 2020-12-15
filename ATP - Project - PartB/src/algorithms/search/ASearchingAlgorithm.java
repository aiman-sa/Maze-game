package algorithms.search;

import java.util.ArrayList;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    protected String name;
    protected int counter;
    protected boolean[][] visitedStates;
    protected AState[][] states;

    protected  ASearchingAlgorithm()
    {
        this.counter=0;
    }
    public int getNumberOfNodesEvaluated()
    {
        return counter;
    }
    public abstract Solution solve(ISearchable is);
    public String getName(){
        return name;
    }

    public ArrayList PathSol(ISearchable domain,AState state){
        if(state==null){
            return null;
        }
        ArrayList<AState> path=new ArrayList<>();
        while(state!=null){
            path.add(0,state);
            state=state.prev;
        }
        if(path.size() == 1 &&path.contains(domain.getGoalState()))
        {
            path.clear();
        }
        return path;
    }
    protected void updateCounterSteps(AState s1,AState s2){
        if(s1==null || s2==null){
            return;
        }
        if(s1.getStepscounter()>s1.getStepscounter()+s2.getStepscounter()){
            s1.setStepscounter(s1.getStepscounter()+s2.getStepscounter());
            s1.setPrev(s2);
        }
    }
    protected void updateCost(AState s1,AState s2){
        if(s1==null || s2==null){
            return;
        }
        if(s1.getCost()>s1.getCost()+s2.getCost()){
            s1.setCost(s1.getCost()+s2.getCost());
            s1.setPrev(s2);
        }
    }
}
