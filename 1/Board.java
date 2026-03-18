import java.util.LinkedList;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int[][] tiles;
    private final int blankRow, blankCol, n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        byte blRow = -1, blCol = -1;
        this.tiles = new int[tiles.length][tiles.length];
        for (byte row = 0; row < tiles.length; row++) {
            for (byte col = 0; col < tiles.length; col++) {
                this.tiles[row][col] = tiles[row][col];
                if (tiles[row][col] == 0) {
                    blRow = row;
                    blCol = col;
                }
            }
        }
        n = tiles.length;
        blankRow = blRow;
        blankCol = blCol;
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(n).append('\n');
        String formatStr = "%" + (Integer.toString(n * n - 1).length() + 1) + "s";
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                str.append(String.format(formatStr, tiles[row][col]));
            }
            str.append('\n');
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    // board dimension n
    public int dimension() { return n; }

    // number of tiles out of place
    public int hamming() {
        int sum = 0;
        for (int row = 0, check = 1; row < n; row++)
            for (int col = 0; col < n; col++)
                if (tiles[row][col] != check++ && tiles[row][col] != 0) sum++;
        return sum;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) continue;
                int num = tiles[row][col];
                int numRow = (num - 1) / n;
                int numCol = (num - 1) % n;
                sum += Math.abs(numRow - row) + Math.abs(numCol - col);
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() { return hamming() == 0; }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) return false;
        Board new_y = (Board) y;
        if (new_y.dimension() != this.dimension()) return false;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] != new_y.tiles[row][col]) return false;
            }
        }
        return true;  
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final int[][] dirs = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};
        LinkedList<Board> result = new LinkedList<Board>();
        for (int[] dir : dirs) {
            int dr = dir[0], dc = dir[1];
            if (blankRow + dr < 0 || blankRow + dr >= n || blankCol + dc < 0 || blankCol + dc >= n) continue;
            int[][] copyTiles = new int[n][n];
            for (int i = 0; i < n; i++) copyTiles[i] = tiles[i].clone();
            int temp = copyTiles[blankRow][blankCol];
            copyTiles[blankRow][blankCol] = copyTiles[blankRow + dr][blankCol + dc];
            copyTiles[blankRow + dr][blankCol + dc] = temp;
            result.add(new Board(copyTiles));
        }
        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board copy = new Board(tiles);
        int swapRow = blankRow == 0 ? 1 : 0;
        int temp = copy.tiles[swapRow][0];
        copy.tiles[swapRow][0] = copy.tiles[swapRow][1];
        copy.tiles[swapRow][1] = temp;
        return copy;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{2, 4, 1}, {7, 0, 6}, {8, 3, 5}};
        Board b = new Board(tiles);
        StdOut.println(b);
        for (Board n : b.neighbors()) {
            StdOut.println(n);
        }
        System.out.println("Done!");
    }

}