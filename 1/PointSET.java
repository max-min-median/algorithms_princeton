import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class PointSET {

    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() { return this.size() == 0; }

    // number of points in the set
    public int size() { return points.size(); }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called insert() with null");
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called insert() with null");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }    
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("called range() with null");
        LinkedList<Point2D> inside = new LinkedList<>();
        for (Point2D p : points) if (rect.contains(p)) inside.add(p);
        return inside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called nearest() with null");
        if (this.isEmpty()) return null;
        
        Point2D nearest = points.iterator().next();
        double minDistSquared = nearest.distanceSquaredTo(p);

        for (Point2D p2 : points) {
            double distSquared = p2.distanceSquaredTo(p);
            if (distSquared < minDistSquared) {
                minDistSquared = distSquared;
                nearest = p2;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}