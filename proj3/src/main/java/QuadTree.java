/**
 * Created by samanthanguyen on 4/17/17.
 */

import java.util.ArrayList;

public class QuadTree {
    Node root;
    ArrayList<Node> imgsRastered;
    int imgWidth;
    int imgHeight;

    public QuadTree(Key rootUpperLeft, Key rootLowerRight) {
        imgsRastered = new ArrayList<>();
        imgWidth = 0;
        imgHeight = 0;
        root = new Node("root", rootUpperLeft, rootLowerRight);
        root.createChildren();
    }

    public void clearTree() {
        imgsRastered.clear();
    }

    public int getWidth() {
        int counter = 0;
        for (int i = 0; i < imgsRastered.size(); i++) {
            if (imgsRastered.get(0).uLPoint.latitude ==
                    imgsRastered.get(i).uLPoint.latitude) {
                counter++;
            }
        }
        imgWidth = counter * MapServer.TILE_SIZE;
        return imgWidth;
    }

    public int getHeight() {
        int counter = 0;
        for (int i = 0; i < imgsRastered.size(); i++) {
            if (imgsRastered.get(0).uLPoint.longitude ==
                    imgsRastered.get(i).uLPoint.longitude) {
                counter++;
            }
        }
        imgHeight = counter * MapServer.TILE_SIZE;
        return imgHeight;
    }

    public void getRasteredImages(Node first, int qDepth, Query currQuery) {
        if (first.intersects(currQuery.qNode)) {
            if (first.checkDepth(qDepth) || first.isLeaf()) {
                imgsRastered.add(first);
            } else {
                for (int i = 0; i < first.children.size(); i++) {
                    getRasteredImages(first.children.get(i), qDepth, currQuery);
                }
            }
        }
    }
}