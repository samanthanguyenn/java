import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {

    HashMap<String, GraphNode> map;
//    public HashMap<String, GraphNode> points;
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        map = new HashMap<>();
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
            map = gbh.nodes;
            clean();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Iterator<HashMap.Entry<String, GraphNode>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            HashMap.Entry<String, GraphNode> entry = iter.next();
            if (entry.getValue().adjacents.size() == 0) {
                iter.remove();
            }
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     */
    Iterable<Long> vertices() {
        ArrayList<Long> vertexIDs = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            vertexIDs.add(map.get(i).id);
        }
        return vertexIDs;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     */
    Iterable<Long> adjacent(long v) {
        ArrayList<Long> adjacents = new ArrayList<>();
        for (int i = 0; i < map.get(v).adjacents.size(); i++) {
            adjacents.add(map.get(v).adjacents.get(i).id);
        }
        return adjacents;
    }

    double distance(long v, long w) {
        String vString = Long.toString(v);
        String wString = Long.toString(w);
        double vLon = map.get(vString).getLon();
        double vLat = map.get(vString).getLat();
        double wLon = map.get(wString).getLon();
        double wLat = map.get(wString).getLat();
        return Math.sqrt(Math.pow((wLon - vLon), 2)
                + Math.pow((wLat - vLat), 2));
    }

    /**
     * Returns the vertex id closest to the given longitude and latitude.
     */
    long closest(double lon, double lat) {
        double min = Math.pow(2, 10000);
        Set locations = map.keySet();
        Iterator iter = locations.iterator();
        String closest = null;
        while (iter.hasNext()) {
            String curr = (String) iter.next();
            double euc = Math.sqrt(Math.pow((lon - map.get(curr).getLon()), 2)
                    + Math.pow((lat - map.get(curr).getLat()), 2));
            if (euc < min) {
                closest = curr;
                min = euc;
            }
        }
        return Long.parseLong(closest);
    }

    GraphNode getGraphNode(String idNum) {
        return map.get(idNum);
    }

    /**
     * Longitude of vertex v.
     */
    double lon(long v) {
        String vString = Long.toString(v);
        return map.get(vString).getLon();
    }

    /**
     * Latitude of vertex v.
     */
    double lat(long v) {
        String vString = Long.toString(v);
        return map.get(vString).getLat();
    }
}
