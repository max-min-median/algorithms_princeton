import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private int opened;
    private boolean[][] grid;
    private boolean perc;
    private WeightedQuickUnionUF qf, qfNoBtm;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        perc = false;
        opened = 0;
        grid = new boolean[n][n];
        qf = new WeightedQuickUnionUF(n * n + 2);
        qfNoBtm = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 1; i <= n; i++) {
            qf.union(0, i);
            qf.union(n * n + 1, n * n + 1 - i);
            qfNoBtm.union(0, i);
        }
    }

    private int idx(int row, int col) {
        return row * n + col + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        row--;
        col--;
        opened++;
        grid[row][col] = true;
        if (row >= 1 && grid[row - 1][col]) {
            qf.union(idx(row - 1, col), idx(row, col));
            qfNoBtm.union(idx(row - 1, col), idx(row, col));
        }
        if (row < n - 1 && grid[row + 1][col]) {
            qf.union(idx(row + 1, col), idx(row, col));
            qfNoBtm.union(idx(row + 1, col), idx(row, col));
        }
        if (col >= 1 && grid[row][col - 1]) {
            qf.union(idx(row, col - 1), idx(row, col));
            qfNoBtm.union(idx(row, col - 1), idx(row, col));
        }
        if (col < n - 1 && grid[row][col + 1]) {
            qf.union(idx(row, col + 1), idx(row, col));
            qfNoBtm.union(idx(row, col + 1), idx(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        try {
            return grid[row - 1][col - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) return false;
        return qfNoBtm.find(idx(row - 1, col - 1)) == qfNoBtm.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opened;
    }

    // does the system percolate?
    public boolean percolates() {
        if (perc) return true;
        if (opened == 0) return false;
        return qf.find(n * n + 1) == qf.find(0);
    }

    // test client (optional)
    public static void main(String[] args) {
        // Percolation p1 = new Percolation(1);
        // Percolation p2 = new Percolation(2);
    }
}