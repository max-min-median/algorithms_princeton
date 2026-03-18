import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int size = 0;
    private int capacity = 2;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() { return size == 0; }

    // return the number of items on the randomized queue
    public int size() { return size; }

    private void resize(int newCapacity) {
        Item[] newRQ = (Item[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) newRQ[i] = q[i];
        capacity = newCapacity;
        q = newRQ;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Unable to add invalid item!");
        if (size() == capacity) resize(2 * capacity);
        q[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) throw new NoSuchElementException("RandomizedQueue is empty!");
        int i = StdRandom.uniformInt(size);
        Item item = q[i];
        q[i] = q[size - 1];
        q[size - 1] = null;
        size--;
        if (size * 4 < capacity && capacity > 1) resize(capacity / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) throw new NoSuchElementException("RandomizedQueue is empty!");
        return q[StdRandom.uniformInt(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new RQIterator(); }

    private class RQIterator implements Iterator<Item> {
        int counter = 0;
        int[] seq = StdRandom.permutation(size);
        public boolean hasNext() { return counter < size; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements in iterator!");
            return q[seq[counter++]];
        }
    }

    private void print() {
        for (Item i : this) {
            System.out.print(i);
            System.out.print(' ');
        }
        System.out.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 1; i <= 10; i++) rq.enqueue(i);
        rq.print();  // "1 2 3 4 5 6 7 8"
        while (!rq.isEmpty()) {
            System.out.print("Removing random: ");
            System.out.print(rq.dequeue() + "\n");
            rq.print();
        }
    }
}