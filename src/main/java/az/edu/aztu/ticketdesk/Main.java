package az.edu.aztu.ticketdesk;

import az.edu.aztu.ticketdesk.structures.ChainedHashTable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class Main {
    private Main() { }
    public static void main(String[] args) throws IOException {
        var out=new PrintStream(System.out,true,StandardCharsets.UTF_8);
        if(args.length==1 && args[0].equals("--demo")) { demo(out); return; }
        if(args.length>=1 && args.length<=2 && args[0].equals("--benchmark")) { Benchmarks.run(Path.of(args.length==2?args[1]:"benchmarks/results"),false,out); return; }
        if(args.length>0) { out.println("İstifadə: java -jar target/ticket-dispatcher-1.0.0.jar [--demo | --benchmark [qovluq]]"); System.exit(2); }
        run(new InputStreamReader(System.in,StandardCharsets.UTF_8),out);
    }
    public static void run(Reader input,PrintStream out) throws IOException {
        var desk=new TicketDesk(); var reader=new BufferedReader(input);
        out.println("Ticket Dispatcher | M656aS | Java 21");
        out.println("Məlumatlar yalnız cari sessiyada saxlanılır. help — əmrlər.");
        String line;
        while(true) {
            out.print("ticket> "); out.flush(); line=reader.readLine();
            if(line==null) break;
            line=line.strip(); if(line.isEmpty()) continue;
            String[] parts=line.split("\\s+",2);
            try {
                if(parts.length>1 && switch(parts[0]) {
                    case "quit","exit","help","process","undo","list","ordered","stats" -> true;
                    default -> false;
                }) throw new IllegalArgumentException("Bu əmr əlavə arqument qəbul etmir.");
                switch(parts[0]) {
                    case "quit", "exit" -> { out.println("Proqram bitdi."); return; }
                    case "help" -> help(out);
                    case "new" -> out.println(desk.create(argument(parts)));
                    case "find" -> show(out,desk.find(id(argument(parts))));
                    case "delete" -> show(out,desk.delete(id(argument(parts))));
                    case "triage" -> {
                        String mode=argument(parts);
                        if(!mode.equals("normal") && !mode.equals("urgent")) throw new IllegalArgumentException("triage normal | urgent");
                        show(out,desk.triageNext(mode.equals("urgent")));
                    }
                    case "process" -> show(out,desk.processNext());
                    case "edit" -> {
                        String[] edit=argument(parts).split("\\s+",2);
                        if(edit.length<2) throw new IllegalArgumentException("edit ID YENİ_BAŞLIQ");
                        show(out,desk.rename(id(edit[0]),edit[1]));
                    }
                    case "undo" -> show(out,desk.undoRename());
                    case "list", "ordered" -> {
                        var list=parts[0].equals("ordered")?desk.ordered():desk.all();
                        if(list.size()==0) out.println("Siyahı boşdur.");
                        for(int i=0;i<list.size();i++) out.println(list.get(i));
                    }
                    case "stats" -> out.printf("Cəmi: %d | Yeni: %d | Planda: %d | Bitmiş: %d | BST hündürlüyü: %d%n",desk.count(),desk.count(Ticket.Status.NEW),desk.count(Ticket.Status.PLANNED),desk.count(Ticket.Status.DONE),desk.treeHeight());
                    default -> out.println("Naməlum əmr. help yazın.");
                }
            } catch(IllegalArgumentException e) { out.println("Xəta: " + e.getMessage()); }
        }
    }
    private static String argument(String[] parts) {
        if(parts.length<2 || parts[1].isBlank()) throw new IllegalArgumentException("Arqument çatışmır. help yazın.");
        return parts[1];
    }
    private static int id(String value) {
        int id=Integer.parseInt(value); if(id<1) throw new IllegalArgumentException("ID müsbət tam ədəd olmalıdır."); return id;
    }
    private static void show(PrintStream out,Ticket ticket) { out.println(ticket==null?"Uyğun sorğu yoxdur.":ticket); }
    private static void help(PrintStream out) {
        out.println("""
                new BAŞLIQ           — yeni sorğu (FIFO Queue)
                triage normal|urgent — növbəti yenini iş planına keçir (Deque)
                process              — plandakı ilk sorğunu tamamla
                edit ID BAŞLIQ       — başlığı dəyiş, əvvəlkini Stack-də saxla
                undo                 — son başlıq dəyişikliyini geri al
                find ID / delete ID  — Hash Table ilə tap / indekslərdən sil
                list / ordered       — Dynamic Array / BST ilə siyahı
                stats                — saylar və BST hündürlüyü
                quit                 — çıxış (məlumatlar saxlanılmır)""");
    }
    public static void demo(PrintStream out) {
        var desk=new TicketDesk();
        out.println("=== TICKET DISPATCHER: real demo ===");
        out.println("CREATE: " + desk.create("Printer işləmir"));
        out.println("CREATE: " + desk.create("Şəbəkə bağlantısı yoxdur"));
        out.println("CREATE: " + desk.create("Monitor problemi"));
        out.println("QUEUE normal: " + desk.triageNext(false));
        out.println("QUEUE urgent -> DEQUE front: " + desk.triageNext(true));
        out.println("QUEUE normal -> DEQUE back: " + desk.triageNext(false));
        out.println("PROCESS: " + desk.processNext());
        out.println("EDIT: " + desk.rename(1,"Printer: kabel yoxlanılsın"));
        out.println("STACK undo: " + desk.undoRename());
        out.println("HASH find(1): " + desk.find(1));
        out.println("DELETE planned #3: " + desk.delete(3));
        out.println("PROCESS: " + desk.processNext());
        out.println("PROCESS (deleted entry skipped): " + desk.processNext());
        var ordered=desk.ordered(); out.println("BST ordered, height=" + desk.treeHeight());
        for(int i=0;i<ordered.size();i++) out.println("  " + ordered.get(i));
        var collisions=new ChainedHashTable<Integer,String>(4);
        collisions.put(1,"A"); collisions.put(5,"B"); collisions.put(9,"C");
        out.printf("HASH collision: capacity=%d, chain=%d, get(9)=%s%n",collisions.capacity(),collisions.maxChainLength(),collisions.get(9));
    }
}
