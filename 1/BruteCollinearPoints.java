import java.util.Arrays;
import java.util.LinkedList;
// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {      // finds all line segments containing 4 points

    private final Point[] points;
    private LineSegment[] segments = null;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Constructor: Points array cannot be null");
        for (int i = 0; i < points.length; i++)
            if (points[i] == null) throw new IllegalArgumentException("Constructor: Elements in points array cannot be null.");
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
        for (int i = 0; i < points.length - 1; i++)
            if (this.points[i].compareTo(this.points[i + 1]) == 0) throw new IllegalArgumentException("Constructor: Duplicate points not allowed.");
    }

    public int numberOfSegments() {
    // the number of line segments
        return segments != null ? segments.length : segments().length;
    }

    public LineSegment[] segments() {
    // the line segments
        if (segments != null) return segments.clone();
        LinkedList<LineSegment> temp = new LinkedList<>();
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                for (int k = j + 1; k < points.length; k++)
                    for (int m = k + 1; m < points.length; m++)
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) && points[i].slopeTo(points[j]) == points[i].slopeTo(points[m]))
                            temp.add(new LineSegment(points[i], points[m]));
        segments = temp.toArray(new LineSegment[0]);
        return segments.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // Point[] points = new Point[n];
        // for (int i = 0; i < n; i++) {
        //     int x = in.readInt();
        //     int y = in.readInt();
        //     points[i] = new Point(x, y);
        // }

        // draw the points
        // StdDraw.enableDoubleBuffering();
        // StdDraw.setXscale(0, 32768);
        // StdDraw.setYscale(0, 32768);
        // for (Point p : points) {
        //     p.draw();
        // }
        // StdDraw.show();

        // print and draw the line segments
        // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        //     StdOut.println(segment);
        //     segment.draw();
        // }
        // StdDraw.show();
    }
}