import java.util.Iterator;
import java.util.NoSuchElementException;

public class DequeArray<Item> implements Iterable<Item> {

    private Item[] q;
    private int start = 0;
    private int size = 0;
    private int capacity = 2;

    // construct an empty deque
    public DequeArray() {
        q = (Item[]) new Object[capacity];
    }

    // is the deque empty?
    public boolean isEmpty() { return size == 0; }

    // return the number of items on the deque
    public int size() { return size; }

    private void resize(int newCapacity) {
        Item[] newDeque = (Item[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newDeque[i] = q[start];
            start++;
            if (start == capacity) start = 0;
        }
        start = 0;
        capacity = newCapacity;
        q = newDeque;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Unable to add invalid item!");
        if (size() == capacity) resize((int) (2 * capacity));
        if (--start < 0) start += capacity;
        q[start] = item;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Unable to add invalid item!");
        if (size() == capacity) resize((int) (2 * capacity));
        int i = start + size;
        if (i >= capacity) i -= capacity;
        q[i] = item;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty!");
        Item item = q[start];
        q[start] = null;
        if (++start == capacity) start = 0;
        size--;
        if (size * 4 < capacity && capacity > 1) resize(capacity / 2);
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty!");
        int i = start + size - 1;
        if (i >= capacity) i -= capacity;
        Item item = q[i];
        q[i] = null;
        size--;
        if (size * 4 < capacity && capacity > 1) resize(capacity / 2);
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() { return new DequeIterator(); }

    private class DequeIterator implements Iterator<Item> {
        int counter = 0;
        public boolean hasNext() { return counter < size; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements in iterator!");
            int idx = start + (counter++);
            if (idx >= capacity) idx -= capacity;
            return q[idx];
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
        DequeArray<Integer> d = new DequeArray<>();
        d.addFirst(5);
        d.addFirst(4);
        d.addFirst(3);
        d.addFirst(2);
        d.addFirst(1);
        d.addLast(6);
        d.addLast(7);
        d.addLast(8);
        d.print();  // "1 2 3 4 5 6 7 8"
        System.out.print("Removing first: ");
        System.out.print(d.removeFirst() + "\n");
        System.out.print("Removing last: ");
        System.out.print(d.removeLast() + "\n");
        d.print();
    }

}