import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double z975 = 1.96;
    private int trials;
    private double[] frac;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n < 0 || trials <= 0) throw new IllegalArgumentException();
        int[] opened = new int[trials];
        frac = new double[trials];
        this.trials = trials;
        
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            int[] toOpen = StdRandom.permutation(n * n);
            
            for (opened[i] = 0; !perc.percolates(); opened[i]++) {
                int row = toOpen[opened[i]] / n + 1, col = toOpen[opened[i]] % n + 1;
                perc.open(row, col);
            }
            // sum += opened[i];
            // sum_sq += opened[i] * opened[i];
            frac[i] = (double) opened[i] / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(frac);
        // return (double) sum / (trials * N * N);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(frac);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - z975 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + z975 * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]), trials = Integer.parseInt(args[1]);
        PercolationStats pStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + pStats.mean());
        System.out.println("stddev                  = " + pStats.stddev());
        System.out.println("95% confidence interval = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + "]");
    }
}