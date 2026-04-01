import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int BITS_IN_A_BYTE = 8;
    private int n;
    private int opened;
    private byte[] gridbytes;
    private boolean perc;
    private WeightedQuickUnionUF uf;
    private int[] bottomRowElements;
    private int bottomRowElementCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        perc = false;
        opened = 0;
        gridbytes = new byte[(n * n + BITS_IN_A_BYTE - 1) / BITS_IN_A_BYTE];
        bottomRowElements = new int[(n + 1) / 2 + 1];  // small memory optimization
        // Each grid tile represented by 1 bit. 1 byte = 8 tiles.
        uf = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 1; i <= n; i++) {
            uf.union(0, i);
        }
    }

    private boolean getTile(int row, int col) {
    // Each grid tile represented by 1 bit. 1 byte = 8 tiles.
    // `row` and `col` arguments are 1-indexed!
        int gridIdx = (row - 1) * n + (col - 1);
        return ((gridbytes[gridIdx / BITS_IN_A_BYTE] >> (gridIdx % BITS_IN_A_BYTE)) & 0b1) == 1;
    }

    private void openTile(int row, int col) {
    // `row` and `col` arguments are 1-indexed!
        int gridIdx = (row - 1) * n + (col - 1);
        gridbytes[gridIdx / BITS_IN_A_BYTE] |= (1 << (gridIdx % BITS_IN_A_BYTE));
    }

    private void carefulUnion(int[] myElemInfo, int otherElem) {

        otherElem = uf.find(otherElem);
        if (myElemInfo[0] == otherElem) return;

        int otherBottomRowElementIndex = findBottomElementArrayIndex(otherElem);
        int lowIdx = Math.min(myElemInfo[1], otherBottomRowElementIndex);
        int highIdx = Math.max(myElemInfo[1], otherBottomRowElementIndex);
        // How we proceed depends on whether none, one or both tiles are connected to bottoms.
        if (lowIdx >= 0 && highIdx >= 0) {
            // If both elements are connected to different bottom elements, then after union we will be left with only 1 of them.
            bottomRowElementCount--;
            bottomRowElements[highIdx] = bottomRowElements[bottomRowElementCount];
            uf.union(myElemInfo[0], otherElem);
            bottomRowElements[lowIdx] = uf.find(myElemInfo[0]);
            myElemInfo[0] = bottomRowElements[lowIdx];
            myElemInfo[1] = lowIdx;
        } else if (lowIdx == -1 && highIdx == -1) {
            // If both elements aren't bottom elements, just union.
            uf.union(myElemInfo[0], otherElem);
            myElemInfo[0] = uf.find(myElemInfo[0]);
        } else {
            // One of the elements is a bottom element (lowIdx = -1), so we use highIdx.
            uf.union(myElemInfo[0], otherElem);
            bottomRowElements[highIdx] = uf.find(myElemInfo[0]);
            myElemInfo[0] = bottomRowElements[highIdx];
            myElemInfo[1] = highIdx;
        }
    }

    private int findBottomElementArrayIndex(int element) {
    // returns -1 if element is not connected to bottom row
        for (int i = 0; i < bottomRowElementCount; i++)
            if (bottomRowElements[i] == element) return i;
        return -1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException();
        if (isOpen(row, col)) return;
        opened++;

        openTile(row, col);
        int idx = (row - 1) * n + (col - 1) + 1;

        if (perc) {
            // if system already percolates, we skip the work of tracking bottom row elements
            if (row > 1 && getTile(row - 1, col)) uf.union(idx, idx - n);
            if (row < n && getTile(row + 1, col)) uf.union(idx, idx + n);
            if (col > 1 && getTile(row, col - 1)) uf.union(idx, idx - 1);
            if (col < n && getTile(row, col + 1)) uf.union(idx, idx + 1);
        } else {
            int[] myElemInfo = new int[]{idx, -1};  // {element, index of element in bottomRowElement[]}
            if (row == 1) myElemInfo[0] = uf.find(idx);
            if (row == n) {
                // add this element to bottomRowElement[]
                bottomRowElements[bottomRowElementCount] = myElemInfo[0];
                myElemInfo[1] = bottomRowElementCount;
                bottomRowElementCount++;
            }

            if (row > 1 && getTile(row - 1, col)) carefulUnion(myElemInfo, idx - n);
            if (row < n && getTile(row + 1, col)) carefulUnion(myElemInfo, idx + n);
            if (col > 1 && getTile(row, col - 1)) carefulUnion(myElemInfo, idx - 1);
            if (col < n && getTile(row, col + 1)) carefulUnion(myElemInfo, idx + 1);

            // Percolation check: if this tile connects to a bottom, check if it connects to the top.
            if (myElemInfo[1] != -1 && myElemInfo[0] == uf.find(0)) perc = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException();
        return getTile(row, col);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && uf.find((row - 1) * n + (col - 1) + 1) == uf.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opened;
    }

    // does the system percolate?
    public boolean percolates() {
        return perc;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 1;
        Percolation p = new Percolation(n);
        p.open(1, 1);
        // Percolation p = new Percolation(5);
        // p.open(1, 2);
        // p.open(2, 2);
        // p.open(2, 3);
        // p.open(3, 4);
        // p.open(4, 4);
        // p.open(5, 4);
        // p.open(2, 4);
        // p.open(1, 5);
        // p.open(5, 2);
        // p.open(4, 2);
        // 0 1 0 0 0
        // 0 1 1 0 0
        // 0 0 0 1 0
        // 0 0 0 1 0
        // 0 0 0 1 0
        for (int r = 1; r <= n; r++) {
            for (int c = 1; c <= n; c++) {
                System.out.print((p.isOpen(r, c) ? 1 : 0) + (p.isFull(r, c) ? 1 : 0));
            }
        System.out.println();
        }
        System.out.println(p.percolates() ? "Percolates!" : "No percolation");

    }
}