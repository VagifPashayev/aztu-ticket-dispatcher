package az.edu.aztu.ticketdesk.structures;

import java.util.Objects;

public final class DoublyLinkedList<E> implements Sequence<E> {
    private static final class Node<E> {
        final E value; Node<E> previous, next;
        Node(E value) { this.value = value; }
    }
    private Node<E> head, tail;
    private int size;
    public int size() { return size; }
    private Node<E> nodeAt(int index) {
        Sequence.checkIndex(index, size);
        if (index < size / 2) { Node<E> n = head; for (int i = 0; i < index; i++) n = n.next; return n; }
        Node<E> n = tail; for (int i = size - 1; i > index; i--) n = n.previous; return n;
    }
    public E get(int index) { return nodeAt(index).value; }
    public void addFirst(E value) { insert(0, value); }
    public void addLast(E value) { insert(size, value); }
    public E removeFirst() { return remove(0); }
    public E removeLast() { return remove(size - 1); }
    public void insert(int index, E value) {
        Sequence.checkIndex(index, size + 1); Objects.requireNonNull(value);
        Node<E> n = new Node<>(value);
        Node<E> next = index == size ? null : nodeAt(index);
        Node<E> previous = next == null ? tail : next.previous;
        n.previous = previous; n.next = next;
        if (previous == null) head = n; else previous.next = n;
        if (next == null) tail = n; else next.previous = n;
        size++;
    }
    public E remove(int index) {
        Node<E> n = nodeAt(index);
        if (n.previous == null) head = n.next; else n.previous.next = n.next;
        if (n.next == null) tail = n.previous; else n.next.previous = n.previous;
        size--; return n.value;
    }
    public int indexOf(E value) {
        Node<E> n = head;
        for (int i = 0; n != null; i++, n = n.next) if (n.value.equals(value)) return i;
        return -1;
    }
}
