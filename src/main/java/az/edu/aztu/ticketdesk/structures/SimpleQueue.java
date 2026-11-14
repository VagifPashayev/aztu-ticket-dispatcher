package az.edu.aztu.ticketdesk.structures;

import java.util.NoSuchElementException;

public final class SimpleQueue<E> {
    private final SinglyLinkedList<E> items = new SinglyLinkedList<>();
    public int size() { return items.size(); }
    public boolean isEmpty() { return size()==0; }
    public void offer(E value) { items.addLast(value); }
    public E poll() { if(isEmpty()) throw new NoSuchElementException("Empty queue"); return items.removeFirst(); }
}
