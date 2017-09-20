/**
 * Created by samanthanguyen on 4/20/17.
 */
import java.util.ArrayList;

// A class to represent each node in the OSM file
public class GraphNode {
    long id;
    double longitude;
    double latitude;
    ArrayList<GraphNode> adjacents;

    public GraphNode(long identity, double lon, double lat) {
        id = identity;
        longitude = lon;
        latitude = lat;
        adjacents = new ArrayList<>();
    }

    public double getLon() {
        return longitude;
    }

    public double getLat() {
        return latitude;
    }

    public void addAdjacent(GraphNode newNeighbor) {
        adjacents.add(newNeighbor);
    }
}
