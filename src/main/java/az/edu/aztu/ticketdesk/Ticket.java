package az.edu.aztu.ticketdesk;

public final class Ticket {
    public enum Status { NEW, PLANNED, DONE, DELETED }
    private final int id;
    private String title;
    private Status status=Status.NEW;
    Ticket(int id,String title) { this.id=id; this.title=title; }
    public int id() { return id; }
    public String title() { return title; }
    public Status status() { return status; }
    void rename(String title) { this.title=title; }
    void status(Status status) { this.status=status; }
    @Override public String toString() { return "#%d [%s] %s".formatted(id,status,title); }
}
