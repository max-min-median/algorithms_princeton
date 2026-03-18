import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            StdOut.println("Usage: java Permutation k\n\n- k: the number of items in the permutation");
            return;
        }

        int k;
        try {
            k = Integer.parseInt(args[0]);
            if (k < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            StdOut.println("Argument must be a non-negative integer!");
            return;
        }

        RandomizedQueue<String> rq = new RandomizedQueue<>();
        for (int strCount = 1; !StdIn.isEmpty(); strCount++) {
            String s = StdIn.readString();
            if (strCount <= k)
                rq.enqueue(s);
            else if (StdRandom.bernoulli((double) k / strCount)) {
                rq.dequeue();
                rq.enqueue(s);
            }
        }
        
        for (String s : rq) StdOut.println(s);
    }
}