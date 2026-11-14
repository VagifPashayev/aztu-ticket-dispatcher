package az.edu.aztu.ticketdesk;

import az.edu.aztu.ticketdesk.structures.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {
    @Test void allSequencesMatchIndependentListUnderMixedOperations() {
        for (Sequence<Integer> sequence : new Sequence[]{new FixedArray<Integer>(500), new DynamicArray<Integer>(0), new SinglyLinkedList<Integer>(), new DoublyLinkedList<Integer>()}) {
            var expected = new ArrayList<Integer>();
            var random = new Random(41);
            for (int step = 0; step < 700; step++) {
                if (expected.isEmpty() || random.nextBoolean()) {
                    int at = random.nextInt(expected.size() + 1), value = random.nextInt(50);
                    expected.add(at, value); sequence.insert(at, value);
                } else {
                    int at = random.nextInt(expected.size());
                    assertEquals(expected.remove(at), sequence.remove(at));
                }
                assertEquals(expected.size(), sequence.size());
                for (int i = 0; i < expected.size(); i++) assertEquals(expected.get(i), sequence.get(i));
                assertEquals(expected.indexOf(17), sequence.indexOf(17));
            }
        }
    }
    @Test void boundsCapacityNullAndEmptyAreExplicit() {
        Sequence<Integer> fixed = new FixedArray<>(1);
        fixed.add(3);
        assertThrows(IllegalStateException.class, () -> fixed.add(4));
        assertEquals(3, fixed.remove(0)); fixed.add(5); assertEquals(5, fixed.get(0));
        for (Sequence<Integer> s : new Sequence[]{fixed, new DynamicArray<>(), new SinglyLinkedList<>(), new DoublyLinkedList<>()}) {
            assertThrows(IndexOutOfBoundsException.class, () -> s.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> s.insert(s.size()+1, 1));
            assertThrows(NullPointerException.class, () -> s.insert(0, null));
        }
        assertThrows(IllegalArgumentException.class, () -> new FixedArray<>(-1));
    }
    @Test void linkedEndsRemainCorrectAfterRemovingOnlyElement() {
        var s = new SinglyLinkedList<Integer>(); s.addLast(1); assertEquals(1,s.removeFirst()); s.addLast(2); assertEquals(2,s.removeFirst());
        var d = new DoublyLinkedList<Integer>(); d.addFirst(1); assertEquals(1,d.removeLast()); d.addLast(2); d.addFirst(3);
        assertEquals(2,d.removeLast()); assertEquals(3,d.removeFirst()); assertEquals(0,d.size());
    }
}
