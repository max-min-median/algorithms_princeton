import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head = null;
    private Node tail = null;
    private int size = 0;

    private class Node {
        Item item = null;
        Node next = null, prev = null;

        Node(Item i) {
            item = i;
        }
    }

    // construct an empty deque
    // public Deque() { }

    // is the deque empty?
    public boolean isEmpty() { return size == 0; }

    // return the number of items on the deque
    public int size() { return size; }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Unable to add invalid item!");
        if (head == null) {  // empty deque
            head = tail = new Node(item);
        } else {
            head.prev = new Node(item);
            head.prev.next = head;
            head = head.prev;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Unable to add invalid item!");
        if (tail == null) {  // empty deque
            head = tail = new Node(item);
        } else {
            tail.next = new Node(item);
            tail.next.prev = tail;
            tail = tail.next;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty!");
        Item i = head.item;
        if (head.next != null) head.next.prev = null; else tail = null;
        head = head.next;
        size--;
        return i;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty!");
        Item i = tail.item;
        if (tail.prev != null) tail.prev.next = null; else head = null;
        tail = tail.prev;
        size--;
        return i;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() { return new DequeIterator(); }

    private class DequeIterator implements Iterator<Item> {
        Node current = head;
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements in iterator!");
            Item i = current.item;
            current = current.next;
            return i;
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
        Deque<Integer> d = new Deque<>();
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