package az.edu.aztu.ticketdesk.structures;

import java.util.Objects;

public final class ChainedHashTable<K,V> {
    private static final class Entry<K,V> {
        final K key; V value; Entry<K,V> next;
        Entry(K key,V value,Entry<K,V> next) { this.key=key; this.value=value; this.next=next; }
    }
    private Entry<K,V>[] buckets;
    private int size;
    public ChainedHashTable() { this(16); }
    @SuppressWarnings("unchecked")
    public ChainedHashTable(int capacity) {
        if(capacity<1) throw new IllegalArgumentException("Capacity must be positive");
        buckets=(Entry<K,V>[])new Entry<?,?>[capacity];
    }
    public int size() { return size; }
    public int capacity() { return buckets.length; }
    private int bucket(K key) { return Math.floorMod(Objects.requireNonNull(key).hashCode(),buckets.length); }
    public V get(K key) {
        for(Entry<K,V> n=buckets[bucket(key)];n!=null;n=n.next) if(n.key.equals(key)) return n.value;
        return null;
    }
    public V put(K key,V value) {
        Objects.requireNonNull(value); int index=bucket(key);
        for(Entry<K,V> n=buckets[index];n!=null;n=n.next) if(n.key.equals(key)) { V old=n.value; n.value=value; return old; }
        if(size+1>buckets.length*0.75) { resize(); index=bucket(key); }
        buckets[index]=new Entry<>(key,value,buckets[index]); size++; return null;
    }
    public V remove(K key) {
        int index=bucket(key); Entry<K,V> previous=null;
        for(Entry<K,V> n=buckets[index];n!=null;n=n.next) {
            if(n.key.equals(key)) { if(previous==null) buckets[index]=n.next; else previous.next=n.next; size--; return n.value; }
            previous=n;
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K,V>[] old=buckets;
        buckets=(Entry<K,V>[])new Entry<?,?>[Math.multiplyExact(old.length,2)];
        for(Entry<K,V> first:old) {
            Entry<K,V> n=first;
            while(n!=null) { Entry<K,V> next=n.next; int index=bucket(n.key); n.next=buckets[index]; buckets[index]=n; n=next; }
        }
    }
    public int maxChainLength() {
        int max=0;
        for(Entry<K,V> head:buckets) { int length=0; for(Entry<K,V> n=head;n!=null;n=n.next) length++; max=Math.max(max,length); }
        return max;
    }
}
