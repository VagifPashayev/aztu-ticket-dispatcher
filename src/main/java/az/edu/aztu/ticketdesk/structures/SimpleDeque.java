package az.edu.aztu.ticketdesk.structures;

import java.util.NoSuchElementException;

public final class SimpleDeque<E> {
    private final DoublyLinkedList<E> items = new DoublyLinkedList<>();
    public int size() { return items.size(); }
    public boolean isEmpty() { return size()==0; }
    public void addFirst(E value) { items.addFirst(value); }
    public void addLast(E value) { items.addLast(value); }
    public E removeFirst() { if(isEmpty()) throw new NoSuchElementException("Empty deque"); return items.removeFirst(); }
    public E removeLast() { if(isEmpty()) throw new NoSuchElementException("Empty deque"); return items.removeLast(); }
}
