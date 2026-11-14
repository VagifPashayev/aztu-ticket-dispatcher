package az.edu.aztu.ticketdesk.structures;

import java.util.Objects;

public final class SinglyLinkedList<E> implements Sequence<E> {
    private static final class Node<E> {
        final E value; Node<E> next;
        Node(E value) { this.value = value; }
    }
    private Node<E> head, tail;
    private int size;
    public int size() { return size; }
    private Node<E> nodeAt(int index) {
        Sequence.checkIndex(index, size);
        Node<E> n = head;
        for (int i = 0; i < index; i++) n = n.next;
        return n;
    }
    public E get(int index) { return nodeAt(index).value; }
    public void addFirst(E value) { insert(0, value); }
    public void addLast(E value) { insert(size, value); }
    public E removeFirst() { return remove(0); }
    public void insert(int index, E value) {
        Sequence.checkIndex(index, size + 1); Objects.requireNonNull(value);
        Node<E> n = new Node<>(value);
        if (size == 0) { head = tail = n; }
        else if (index == 0) { n.next = head; head = n; }
        else if (index == size) { tail.next = n; tail = n; }
        else { Node<E> previous = nodeAt(index - 1); n.next = previous.next; previous.next = n; }
        size++;
    }
    public E remove(int index) {
        Sequence.checkIndex(index, size);
        Node<E> removed;
        if (index == 0) { removed = head; head = head.next; }
        else { Node<E> previous = nodeAt(index - 1); removed = previous.next; previous.next = removed.next; if (removed == tail) tail = previous; }
        if (--size == 0) tail = null;
        return removed.value;
    }
    public int indexOf(E value) {
        Node<E> n = head;
        for (int i = 0; n != null; i++, n = n.next) if (n.value.equals(value)) return i;
        return -1;
    }
}
