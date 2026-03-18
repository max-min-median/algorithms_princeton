import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class Solver {

    private class Node {
        Board board;
        int moves;
        int manhattan;
        Node prev;

        public Node(Board board, int moves, int manhattan, Node prev) {
            this.board = board;
            this.moves = moves;
            this.manhattan = manhattan;
            this.prev = prev;
        }
    }

    private Node solution = null;
    private boolean isSoluble;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        // Comparator<Node> cmp = (n1, n2) -> Integer.compare(n1.moves + n1.board.hamming(), n2.moves + n2.board.hamming());
        Comparator<Node> cmp = (n1, n2) -> Integer.compare(n1.manhattan, n2.manhattan);
        MinPQ<Node> pq1 = new MinPQ<>(cmp), pq2 = new MinPQ<>(cmp);
        pq1.insert(new Node(initial, 0, initial.manhattan(), null));
        pq2.insert(new Node(initial.twin(), 0, initial.twin().manhattan(), null));
        while (!pq1.isEmpty()) {
            Node current = pq1.delMin();
            if (current.board.isGoal()) {
                solution = current;
                isSoluble = true;
                break;
            }
            for (Board neighbor: current.board.neighbors()) {
                if (current.prev != null && neighbor.equals(current.prev.board)) continue;
                pq1.insert(new Node(neighbor, current.moves + 1, current.moves + 1 + neighbor.manhattan(), current));
            }
            current = pq2.delMin();
            if (current.board.isGoal()) {
                solution = current;
                isSoluble = false;
                break;
            }
            for (Board neighbor: current.board.neighbors()) {
                if (current.prev != null && neighbor.equals(current.prev.board)) continue;
                pq2.insert(new Node(neighbor, current.moves + 1, current.moves + 1 + neighbor.manhattan(), current));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() { return isSoluble; }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solution.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Node current = solution;
        Stack<Board> st = new Stack<>();
        while (current != null) {
            st.push(current.board);
            current = current.prev;
        }
        return st;
    }

    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}
