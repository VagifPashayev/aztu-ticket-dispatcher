package az.edu.aztu.ticketdesk;

import az.edu.aztu.ticketdesk.structures.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class StructuresTest {
    @Test void lifoFifoAndBothDequeEnds() {
        var stack = new SimpleStack<Integer>(); var queue = new SimpleQueue<Integer>(); var deque = new SimpleDeque<Integer>();
        for (int i=1;i<=3;i++) { stack.push(i); queue.offer(i); }
        assertEquals(3,stack.pop()); assertEquals(2,stack.pop()); assertEquals(1,stack.pop());
        assertEquals(1,queue.poll()); assertEquals(2,queue.poll()); assertEquals(3,queue.poll());
        deque.addLast(2); deque.addFirst(1); deque.addLast(3);
        assertEquals(3,deque.removeLast()); assertEquals(1,deque.removeFirst()); assertEquals(2,deque.removeLast());
        assertThrows(NoSuchElementException.class,stack::pop); assertThrows(NoSuchElementException.class,queue::poll); assertThrows(NoSuchElementException.class,deque::removeFirst);
        queue.offer(4); assertEquals(4,queue.poll());
    }
    record CollisionKey(int id) { @Override public int hashCode() { return Integer.MIN_VALUE; } }
    @Test void collisionsUpdatesResizeAndRemovalPreserveOtherKeys() {
        var map = new ChainedHashTable<CollisionKey,Integer>(2);
        for(int i=0;i<100;i++) assertNull(map.put(new CollisionKey(i),i));
        assertEquals(100,map.size()); assertEquals(100,map.maxChainLength());
        assertEquals(50,map.put(new CollisionKey(50),500)); assertEquals(100,map.size());
        assertEquals(500,map.remove(new CollisionKey(50))); assertNull(map.remove(new CollisionKey(500)));
        for(int i=0;i<100;i++) if(i!=50) assertEquals(i,map.get(new CollisionKey(i)));
        assertNull(map.get(new CollisionKey(50)));
        assertThrows(NullPointerException.class,()->map.put(null,1));
    }
    @Test void hashTableMatchesJdkDuringMixedOperations() {
        var actual=new ChainedHashTable<Integer,Integer>(1); var expected=new HashMap<Integer,Integer>(); var random=new Random(83);
        for(int i=0;i<4000;i++) {
            int k=random.nextInt(150);
            if(random.nextBoolean()) assertEquals(expected.put(k,i),actual.put(k,i));
            else assertEquals(expected.remove(k),actual.remove(k));
            assertEquals(expected.size(),actual.size());
            for(int key=0;key<150;key++) assertEquals(expected.get(key),actual.get(key));
        }
    }
    @Test void bstDeletesLeafOneChildTwoChildrenAndRoot() {
        var tree=new BinarySearchTree<String>();
        for(int k:new int[]{8,3,10,1,6,14,4,7,13}) tree.put(k,"v"+k);
        assertEquals("v1",tree.remove(1)); assertEquals("v14",tree.remove(14)); assertEquals("v3",tree.remove(3)); assertEquals("v8",tree.remove(8));
        assertArrayEquals(new int[]{4,6,7,10,13},tree.keysInOrder()); assertEquals(5,tree.size());
        assertNull(tree.remove(999)); assertEquals("v13",tree.get(13));
        assertEquals("v6",tree.put(6,"changed")); assertEquals(5,tree.size());
    }
    @Test void bstMatchesTreeMapAndSkewDoesNotOverflowCallStack() {
        var tree=new BinarySearchTree<Integer>(); var expected=new TreeMap<Integer,Integer>(); var random=new Random(92);
        for(int i=0;i<1500;i++) { int key=random.nextInt(200)-100;
            if(random.nextBoolean()) assertEquals(expected.put(key,i),tree.put(key,i)); else assertEquals(expected.remove(key),tree.remove(key));
            assertArrayEquals(expected.keySet().stream().mapToInt(Integer::intValue).toArray(),tree.keysInOrder());
            assertEquals(expected.size(),tree.size());
            for(int k:expected.keySet()) assertEquals(expected.get(k),tree.get(k));
        }
        var sorted=new BinarySearchTree<Integer>();
        for(int i=0;i<5000;i++) sorted.put(i,i);
        assertEquals(5000,sorted.height()); assertEquals(5000,sorted.searchComparisons(4999));
        assertEquals(5000,sorted.keysInOrder().length); assertEquals(0,sorted.remove(0));
    }
}
