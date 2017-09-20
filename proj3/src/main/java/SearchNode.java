/**
 * Created by samanthanguyen on 4/20/17.
 */
public class SearchNode implements Comparable<SearchNode> {
    GraphNode startNode;
    double test;

    public SearchNode(GraphNode curr, GraphNode target, double distanceTraveled) {
        test = distanceTraveled + eucDistHeuristic(curr, target);
        startNode = curr;
    }

    @Override
    public int compareTo(SearchNode x) {
        return Double.compare(this.test, x.test);
    }

    private double eucDistHeuristic(GraphNode x, GraphNode y) {
        return Math.sqrt(Math.pow((y.getLon() - x.getLon()), 2)
                + Math.pow((y.getLat() - x.getLat()), 2));
    }
}
