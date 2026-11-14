package az.edu.aztu.ticketdesk.structures;

public interface Sequence<E> {
    int size();
    E get(int index);
    void insert(int index, E value);
    E remove(int index);
    int indexOf(E value);
    default void add(E value) { insert(size(), value); }
    static void checkIndex(int index, int upperExclusive) {
        if (index < 0 || index >= upperExclusive) throw new IndexOutOfBoundsException("index=" + index);
    }
}
