package az.edu.aztu.ticketdesk.structures;

import java.util.Objects;

public final class DynamicArray<E> implements Sequence<E> {
    private Object[] data;
    private int size;
    public DynamicArray() { this(8); }
    public DynamicArray(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Negative capacity");
        data = new Object[Math.max(1, capacity)];
    }
    public int size() { return size; }
    public int capacity() { return data.length; }
    @SuppressWarnings("unchecked")
    public E get(int index) { Sequence.checkIndex(index, size); return (E) data[index]; }
    public void insert(int index, E value) {
        Sequence.checkIndex(index, size + 1); Objects.requireNonNull(value);
        if (size == data.length) {
            Object[] larger = new Object[Math.multiplyExact(data.length, 2)];
            System.arraycopy(data, 0, larger, 0, size); data = larger;
        }
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = value; size++;
    }
    public E remove(int index) {
        E old = get(index);
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null; return old;
    }
    public int indexOf(E value) {
        for (int i = 0; i < size; i++) if (data[i].equals(value)) return i;
        return -1;
    }
}
