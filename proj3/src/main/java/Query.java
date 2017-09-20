/**
 * Created by samanthanguyen on 4/17/17.
 */
public class Query {
    Node qNode;
    double lonDPP;

    public Query(Key ul, Key lr, double width) {
        qNode = new Node("query", ul, lr);
        lonDPP = (lr.longitude - ul.longitude) / width;
    }

    public int getDepth(double qLonDPP, double lRLON, double uLLON) {
        int depth = 0;
        double dPP = (lRLON - uLLON) / (Math.pow(2, depth) * MapServer.TILE_SIZE);
        for (; depth < 7 && dPP > qLonDPP; depth++) {
            dPP = dPP / 2;
        }
        return depth;
    }
}