package az.edu.aztu.ticketdesk;

import az.edu.aztu.ticketdesk.structures.*;

public final class TicketDesk {
    private record Rename(int id,String oldTitle) { }
    private final DynamicArray<Ticket> catalog=new DynamicArray<>();
    private final ChainedHashTable<Integer,Ticket> byId=new ChainedHashTable<>();
    private final BinarySearchTree<Ticket> tree=new BinarySearchTree<>();
    private final SimpleQueue<Ticket> incoming=new SimpleQueue<>();
    private final SimpleDeque<Ticket> work=new SimpleDeque<>();
    private final SimpleStack<Rename> history=new SimpleStack<>();
    private int nextId=1;
    private static String validTitle(String title) {
        if(title==null || title.isBlank() || title.strip().length()>200 || title.chars().anyMatch(Character::isISOControl))
            throw new IllegalArgumentException("Başlıq 1–200 simvol olmalı, sətirsonu işarəsi daşımamalıdır.");
        return title.strip();
    }
    public Ticket create(String title) {
        String checked=validTitle(title); int id=nextId; nextId=Math.incrementExact(nextId);
        Ticket ticket=new Ticket(id,checked); catalog.add(ticket); byId.put(id,ticket); tree.put(id,ticket); incoming.offer(ticket);
        return ticket;
    }
    public Ticket find(int id) { return byId.get(id); }
    public int count() { return catalog.size(); }
    public int treeHeight() { return tree.height(); }
    public DynamicArray<Ticket> all() {
        var copy=new DynamicArray<Ticket>(count()); for(int i=0;i<count();i++) copy.add(catalog.get(i)); return copy;
    }
    public DynamicArray<Ticket> ordered() {
        var copy=new DynamicArray<Ticket>(count()); for(int id:tree.keysInOrder()) copy.add(tree.get(id)); return copy;
    }
    public int count(Ticket.Status status) {
        int found=0; for(int i=0;i<count();i++) if(catalog.get(i).status()==status) found++; return found;
    }
    public Ticket triageNext(boolean urgent) {
        while(!incoming.isEmpty()) {
            Ticket ticket=incoming.poll();
            if(ticket.status()!=Ticket.Status.NEW) continue;
            ticket.status(Ticket.Status.PLANNED);
            if(urgent) work.addFirst(ticket); else work.addLast(ticket);
            return ticket;
        }
        return null;
    }
    public Ticket processNext() {
        while(!work.isEmpty()) {
            Ticket ticket=work.removeFirst();
            if(ticket.status()!=Ticket.Status.PLANNED) continue;
            ticket.status(Ticket.Status.DONE); return ticket;
        }
        return null;
    }
    public Ticket rename(int id,String title) {
        Ticket ticket=byId.get(id);
        if(ticket==null) throw new IllegalArgumentException("Belə ID yoxdur: " + id);
        String checked=validTitle(title); history.push(new Rename(id,ticket.title())); ticket.rename(checked); return ticket;
    }
    public Ticket undoRename() {
        while(!history.isEmpty()) {
            Rename edit=history.pop(); Ticket ticket=byId.get(edit.id());
            if(ticket!=null) { ticket.rename(edit.oldTitle()); return ticket; }
        }
        return null;
    }
    public Ticket delete(int id) {
        Ticket ticket=byId.remove(id);
        if(ticket==null) return null;
        tree.remove(id); catalog.remove(catalog.indexOf(ticket)); ticket.status(Ticket.Status.DELETED);
        // Queued references are skipped lazily; IDs are never reused and undo cannot restore a deleted ticket.
        return ticket;
    }
}
