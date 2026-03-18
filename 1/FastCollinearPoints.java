import java.util.Arrays;
import java.util.LinkedList;
// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {      // finds all line segments containing 4 points

    private final Point[] points;
    private LineSegment[] segments = null;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Constructor: Points array cannot be null");
        for (int i = 0; i < points.length; i++)
            if (points[i] == null) throw new IllegalArgumentException("Constructor: Elements in points array cannot be null.");
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
        for (int i = 0; i < points.length - 1; i++) {
            if (this.points[i].compareTo(this.points[i + 1]) == 0) throw new IllegalArgumentException("Constructor: Duplicate points not allowed.");
        }
    }

    public int numberOfSegments() {
    // the number of line segments
        return segments != null ? segments.length : segments().length;
    }

    public LineSegment[] segments() {
    // the line segments
        if (segments != null) return segments.clone();
        LinkedList<LineSegment> temp = new LinkedList<>();

        for (int i = 0; i < points.length; i++) {
            Point[] subseqPoints = points.clone();
            Arrays.sort(subseqPoints, points[i].slopeOrder());
            Double prevSlope = null;
            int streak = 0;  // -1: invalid, wait for next slope.
            for (int j = 0; j < subseqPoints.length; j++) {
                // If the new slope is different,
                // - check if streak >= 3 and add the segment if so.
                // else if the new slope is the same as previous,
                // - if streak != -1, increment streak
                // Set prevSlope to slope.
                // Check if point is <= current point (which means the segment would have been considered
                //   in a previous iteration), and set streak = -1 if so.
                // - else increment 'streak'.
                // System.out.println("i: " + points[i] + " j: " + subseqPoints[j] + " Slope: " + slope);
                double slope = points[i].slopeTo(subseqPoints[j]);
                if (prevSlope == null || prevSlope != slope) {
                    if (streak >= 3) {
                        // System.out.println("Adding line segment: " + points[i] + "->" + subseqPoints[j-1]);
                        temp.add(new LineSegment(points[i], subseqPoints[j-1]));
                    }
                    streak = 1;
                } else {
                    if (streak != -1) streak++;
                }
                prevSlope = slope;
                if (points[i].compareTo(subseqPoints[j]) >= 0) {
                    // System.out.println("Setting streak = -1");
                    streak = -1;
                }
            }
            if (streak >= 3) temp.add(new LineSegment(points[i], subseqPoints[subseqPoints.length - 1]));
        }
        segments = temp.toArray(new LineSegment[0]);
        return segments.clone();
    }

    public static void main(String[] args) {
        // // read the n points from a file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // Point[] points = new Point[n];
        // for (int i = 0; i < n; i++) {
        //     int x = in.readInt();
        //     int y = in.readInt();
        //     points[i] = new Point(x, y);
        // }

        // // draw the points
        // StdDraw.enableDoubleBuffering();
        // StdDraw.setXscale(0, 32768);
        // StdDraw.setYscale(0, 32768);
        // for (Point p : points) {
        //     p.draw();
        // }
        // StdDraw.show();

        // // print and draw the line segments
        // FastCollinearPoints collinear = new FastCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        //     StdOut.println(segment);
        //     segment.draw();
        // }
        // StdDraw.show();
    }
}