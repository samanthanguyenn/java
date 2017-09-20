import java.util.LinkedList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest,
     * where the longs are node IDs.
     */

    public static LinkedList<Long> shortestPath(GraphDB g,
                    double stlon, double stlat, double destlon, double destlat) {
        Long start = g.closest(stlon, stlat);
        Long dest = g.closest(destlon, destlat);
        String startID = Long.toString(start);
        String destID = Long.toString(dest);
        GraphNode source = g.getGraphNode(startID);
        GraphNode target = g.getGraphNode(destID);
        LinkedList<Long> path = new LinkedList<>();
        HashMap<GraphNode, GraphNode> pathWent = new HashMap<>();
        HashMap<String, GraphNode> visited = new HashMap<>();
        HashMap<GraphNode, Double> distance = new HashMap<>();
        PriorityQueue<SearchNode> fringe = new PriorityQueue<>();
        SearchNode firstSearch = new SearchNode(source, target, 0);
        fringe.add(firstSearch);
        distance.put(source, 0.0);
        pathWent.put(source, source);
        while (!fringe.isEmpty()) {
            GraphNode curr = fringe.poll().startNode;
            if (!visited.containsKey(curr.id)) {
                visited.put(Long.toString(curr.id), curr);
                if (!curr.equals(dest)) {
                    for (int i = 0; i < curr.adjacents.size(); i++) {
                        GraphNode current = curr.adjacents.get(i);
                        double dist = g.distance(curr.id, current.id);
                        double currDist = Math.pow(2, 10000);
                        if (distance.get(current) != null) {
                            currDist = distance.get(current);
                        }
                        if (currDist > distance.get(curr) + dist) {
                            pathWent.put(current, curr);
                            double closer = distance.get(curr) + dist;
                            distance.put(current, closer);
                            fringe.add(new SearchNode(current, target, closer));
                        }
                    }
                }
            }
        }
        path.add(0, target.id);
        GraphNode currNode = target;
        for (GraphNode pred = pathWent.get(currNode);
             pathWent.get(currNode) != currNode; path.add(0, pred.id)) {
            pred = pathWent.get(currNode);
            currNode = pred;
        }
        return path;
    }
}
