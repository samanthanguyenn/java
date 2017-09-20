/**
 * Created by samanthanguyen on 4/17/17.
 */

import java.util.ArrayList;

public class Node implements Comparable<Node> {
    Key uLPoint;
    Key lRPoint;
    int depth;
    Node NW, NE, SE, SW;
    String imgName;
    String wholeImgName;
    ArrayList<Node> children;

    Node(String inputImg, Key upperLeft, Key lowerRight) {
        uLPoint = upperLeft;
        lRPoint = lowerRight;
        imgName = inputImg;
        wholeImgName = "img/" + imgName + ".png";
        children = new ArrayList<>();
        if (imgName.equals("root")) {
            depth = 0;
        } else {
            depth = imgName.length();
        }
    }

    @Override
    public int compareTo(Node x) {
        if (uLPoint.longitude == x.uLPoint.longitude
                && uLPoint.latitude == x.uLPoint.latitude) {
            return 0;
        } else {
            if (uLPoint.latitude == x.uLPoint.latitude) {
                if (x.uLPoint.longitude < uLPoint.longitude) {
                    return 1;
                } else {
                    return -1;
                }
            } else if (x.uLPoint.latitude > uLPoint.latitude) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public boolean intersects(Node other) {
        return !(lRPoint.latitude > other.uLPoint.latitude
                || other.lRPoint.longitude < uLPoint.longitude
                || other.lRPoint.latitude > uLPoint.latitude
                || other.uLPoint.longitude > lRPoint.longitude);
    }

    public void createChildren() {
        if (depth == 7) {
            return;
        } else if (imgName.equals("root")) {
            NW = new Node("1", uLPoint,
                    new Key((uLPoint.longitude + lRPoint.longitude) / 2,
                            (uLPoint.latitude + lRPoint.latitude) / 2));
            NW.createChildren();
            NE = new Node("2", new Key((uLPoint.longitude + lRPoint.longitude) / 2,
                    uLPoint.latitude), new Key(lRPoint.longitude,
                    (uLPoint.latitude + lRPoint.latitude) / 2));
            NE.createChildren();
            SW = new Node("3", new Key(uLPoint.longitude,
                    (uLPoint.latitude + lRPoint.latitude) / 2),
                    new Key((uLPoint.longitude + lRPoint.longitude) / 2, lRPoint.latitude));
            SW.createChildren();
            SE = new Node("4", new Key((uLPoint.longitude + lRPoint.longitude) / 2,
                    (uLPoint.latitude + lRPoint.latitude) / 2), lRPoint);
            SE.createChildren();
        } else {
            NW = new Node(imgName + "1", uLPoint,
                    new Key((uLPoint.longitude + lRPoint.longitude) / 2,
                            (uLPoint.latitude + lRPoint.latitude) / 2));
            NW.createChildren();
            NE = new Node(imgName + "2",
                    new Key((uLPoint.longitude + lRPoint.longitude) / 2, uLPoint.latitude),
                    new Key(lRPoint.longitude, (uLPoint.latitude + lRPoint.latitude) / 2));
            NE.createChildren();
            SW = new Node(imgName + "3", new Key(uLPoint.longitude,
                    (uLPoint.latitude + lRPoint.latitude) / 2),
                    new Key((uLPoint.longitude + lRPoint.longitude) / 2, lRPoint.latitude));
            SW.createChildren();
            SE = new Node(imgName + "4",
                    new Key((uLPoint.longitude + lRPoint.longitude) / 2,
                            (uLPoint.latitude + lRPoint.latitude) / 2), lRPoint);
            SE.createChildren();
        }
        children.add(NW);
        children.add(NE);
        children.add(SW);
        children.add(SE);
    }

    public boolean checkDepth(double queryDepth) {
        return (queryDepth == depth);
    }

    public boolean isLeaf() {
        return depth == 7;
    }

}