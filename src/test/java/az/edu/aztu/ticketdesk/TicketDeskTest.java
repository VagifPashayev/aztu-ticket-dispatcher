package az.edu.aztu.ticketdesk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketDeskTest {
    @Test void incomingIsFifoAndUrgentGoesBeforeOrdinary() {
        var desk=new TicketDesk(); var a=desk.create("Printer"); var b=desk.create("Network"); var c=desk.create("Monitor");
        assertEquals(a.id(),desk.triageNext(false).id()); assertEquals(b.id(),desk.triageNext(true).id()); assertEquals(c.id(),desk.triageNext(false).id());
        assertEquals(b.id(),desk.processNext().id()); assertEquals(a.id(),desk.processNext().id()); assertEquals(c.id(),desk.processNext().id());
        assertEquals(Ticket.Status.DONE,desk.find(b.id()).status()); assertNull(desk.processNext());
    }
    @Test void undoIsLifoAndDeletionNeverResurrectsStaleEntries() {
        var desk=new TicketDesk(); var a=desk.create("A"); var b=desk.create("B");
        desk.rename(a.id(),"A1"); desk.rename(a.id(),"A2");
        assertEquals("A1",desk.undoRename().title()); assertEquals("A",desk.undoRename().title()); assertNull(desk.undoRename());
        desk.rename(a.id(),"A3"); desk.delete(a.id()); assertNull(desk.undoRename());
        assertEquals(b.id(),desk.triageNext(false).id()); assertNull(desk.triageNext(false));
        desk.delete(b.id()); assertNull(desk.processNext()); assertEquals(0,desk.count());
        assertNull(desk.find(a.id())); assertEquals(0,desk.ordered().size()); assertEquals(0,desk.treeHeight());
        var c=desk.create("C"); assertTrue(c.id()>b.id()); assertEquals(1,desk.count());
    }
    @Test void invalidOperationsDoNotMutateState() {
        var desk=new TicketDesk(); assertThrows(IllegalArgumentException.class,()->desk.create("  "));
        var a=desk.create("Valid"); assertEquals(1,a.id());
        assertThrows(IllegalArgumentException.class,()->desk.rename(a.id(),""));
        assertThrows(IllegalArgumentException.class,()->desk.rename(77,"missing"));
        assertEquals("Valid",desk.find(a.id()).title()); assertNull(desk.undoRename()); assertNull(desk.delete(77)); assertEquals(1,desk.count());
    }
}
