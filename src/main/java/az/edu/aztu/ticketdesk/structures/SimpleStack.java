package az.edu.aztu.ticketdesk.structures;

import java.util.NoSuchElementException;

public final class SimpleStack<E> {
    private final DynamicArray<E> items = new DynamicArray<>();
    public int size() { return items.size(); }
    public boolean isEmpty() { return size()==0; }
    public void push(E value) { items.add(value); }
    public E pop() { if(isEmpty()) throw new NoSuchElementException("Empty stack"); return items.remove(size()-1); }
}
