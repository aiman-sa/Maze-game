package algorithms.search;

public interface ISearchingAlgorithm {

    Solution solve(ISearchable searchable);

    int getNumberOfNodesEvaluated();

    public String getName();

}
