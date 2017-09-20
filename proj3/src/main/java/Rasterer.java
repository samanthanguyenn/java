import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private static GraphDB g;
    private static QuadTree imgTree;

    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.

    /**
     * imgRoot is the name of the directory containing the images.
     * You may not actually need this for your class.
     */
    public Rasterer(String imgRoot) {
        // YOUR CODE HERE
        Key rootUpperLeft = new Key(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT);
        Key rootLowerRight = new Key(MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);
        imgTree = new QuadTree(rootUpperLeft, rootLowerRight);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
<<<<<<< HEAD
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     * <li>The tiles collected must cover the most longitudinal distance per pixel
     * (LonDPP) possible, while still covering less than or equal to the amount of
     * longitudinal distance per pixel in the query box for the user viewport size. </li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the
     * above condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     * </p>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     * Can also be interpreted as the length of the numbers in the image
     * string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     * forget to set this to true! <br>
     * //     * @see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        Query currQuery = new Query(new Key(params.get("ullon"), params.get("ullat")),
                new Key(params.get("lrlon"), params.get("lrlat")),
                params.get("w"));

        int qDepth = currQuery.getDepth(currQuery.lonDPP,
            MapServer.ROOT_LRLON, MapServer.ROOT_ULLON);
        imgTree.clearTree();
        imgTree.getRasteredImages(imgTree.root, qDepth, currQuery);
        Collections.sort(imgTree.imgsRastered);
        System.out.println("height: " + imgTree.getHeight());
        System.out.println("width: " + imgTree.getWidth());
        for (int x = 0; x < imgTree.imgsRastered.size(); x++) {
            System.out.println("arrayList print: " + imgTree.imgsRastered.get(x).wholeImgName);
        }
        String[][] toRet = new String[imgTree.getHeight() / 256][imgTree.getWidth() / 256];
        int x = 0;
        for (int i = 0; i < imgTree.getHeight() / 256; i++) {
            for (int j = 0; j < imgTree.getWidth() / 256; j++) {
                toRet[i][j] = imgTree.imgsRastered.get(x).wholeImgName;
                x++;
            }
        }

        results.put("render_grid", toRet);
        results.put("raster_ul_lon", imgTree.imgsRastered.get(0).uLPoint.longitude);
        results.put("raster_ul_lat", imgTree.imgsRastered.get(0).uLPoint.latitude);
        results.put("raster_lr_lon",
                imgTree.imgsRastered.get(imgTree.imgsRastered.size() - 1).lRPoint.longitude);
        results.put("raster_lr_lat",
                imgTree.imgsRastered.get(imgTree.imgsRastered.size() - 1).lRPoint.latitude);
        results.put("depth", qDepth);
        System.out.println("depth: " + qDepth);
        results.put("query_success", true);
        return results;
    }

}
