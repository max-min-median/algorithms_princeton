import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {

    private static final int DIMENSIONS = 2;    
    private class Node { }
    private Point2D point;
    private int dim;
    private int size;
    private KdTree left, right;

    // construct an empty set of points
    public KdTree() { }

    private KdTree(Point2D p, int dimension) {
        if (p != null) {
            point = p;
            size = 1;
            left = new KdTree();
            right = new KdTree();
        }
        dim = dimension % DIMENSIONS;
    }

    // is the set empty?
    public boolean isEmpty() { return size == 0; }

    // number of points in the set
    public int size() { return size; }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called insert() with null");
        insertActual(p);
    }

    private int insertActual(Point2D p) {
        if (point == null) { 
            point = p;
            left = new KdTree(null, dim + 1);
            right = new KdTree(null, dim + 1);
            size++;
            return 1;
        }
        else if (p.equals(point)) return 0;  // point already exists in KdTree, do not insert
        else {
            int inserted = (pGoesLeft(p) ? left : right).insertActual(p);
            size += inserted;
            return inserted;
        }
    };

    private boolean pGoesLeft(Point2D p) { return dimensionalCompare(p, point) < 0; }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called contains() with null");
        else if (point == null) return false;
        else if (p.equals(point)) return true;
        else if (pGoesLeft(p)) return left.contains(p);
        else return right.contains(p);
    }

    // draw all points to standard draw
    public void draw() { drawTree(new RectHV(0, 0, 1, 1)); }

    private void drawTree(RectHV rect) {
        if (point == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(point.x(), point.y());
        if (dim == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
        } else {  // dim == 1
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
        }
        left.drawTree(leftRect(rect));
        right.drawTree(rightRect(rect));
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("called range() with null");
        LinkedList<Point2D> list = new LinkedList<>();
        rectSearch(rect, rect.xmin(), rect.xmax(), rect.ymin(), rect.ymax(), list);
        return list;
    }

    private void rectSearch(RectHV rect, double xMin, double xMax, double yMin, double yMax, LinkedList<Point2D> list) {
        if (point == null) return;
        if (rect.contains(point)) list.add(point);
        if (dim == 0) {
            if (xMin < point.x()) left.rectSearch(rect, xMin, xMax, yMin, yMax, list);
            if (xMax >= point.x()) right.rectSearch(rect, xMin, xMax, yMin, yMax, list);
        } else {
            if (yMin < point.y()) left.rectSearch(rect, xMin, xMax, yMin, yMax, list);
            if (yMax >= point.y()) right.rectSearch(rect, xMin, xMax, yMin, yMax, list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called nearest() with null");
        return nearestSearch(p, Double.POSITIVE_INFINITY, new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    private Point2D nearestSearch(Point2D p, double minSquaredDist, RectHV rect) {
        if (point == null || rect.distanceSquaredTo(p) > minSquaredDist) return null;
        double squaredDist = point.distanceSquaredTo(p);
        Point2D best = null;
        if (squaredDist < minSquaredDist) {
            best = point;
            minSquaredDist = squaredDist;
        }
        boolean leftFirst = pGoesLeft(p);
        if (leftFirst) {
            Point2D pointFromLeft = left.nearestSearch(p, minSquaredDist, leftRect(rect));
            if (pointFromLeft != null) minSquaredDist = (best = pointFromLeft).distanceSquaredTo(p);
        }
        Point2D pointFromRight = right.nearestSearch(p, minSquaredDist, rightRect(rect));
        if (pointFromRight != null) minSquaredDist = (best = pointFromRight).distanceSquaredTo(p);
        if (!leftFirst) {
            Point2D pointFromLeft = left.nearestSearch(p, minSquaredDist, leftRect(rect));
            if (pointFromLeft != null) best = pointFromLeft;
        }
        return best;
    }

    private int dimensionalCompare(Point2D p1, Point2D p2) {
        if (dim == 0) return Double.compare(p1.x(), p2.x());
        return Double.compare(p1.y(), p2.y());
    }
    
    private RectHV leftRect(RectHV rect) {
        if (dim == 0) return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
        else return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
    }

    private RectHV rightRect(RectHV rect) {
        if (dim == 0) return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        else return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = "input10.txt";
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        Point2D p = new Point2D(0.3, 0.3);
        Point2D nearest = kdtree.nearest(p);
        System.out.println(nearest);
    }
}