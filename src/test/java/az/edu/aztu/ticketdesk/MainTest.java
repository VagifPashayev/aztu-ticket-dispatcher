package az.edu.aztu.ticketdesk;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test void scriptHandlesErrorsAndEofWithoutLosingTickets() throws Exception {
        String input="new Printer\nnew Network\ntriage normal\ntriage urgent\nprocess\nedit 1 Printer fixed\nundo\nfind invalid\ntriage invalid\nwat\nfind 1\nordered\nstats\n";
        var bytes=new ByteArrayOutputStream();
        Main.run(new StringReader(input),new PrintStream(bytes,true,StandardCharsets.UTF_8));
        String out=bytes.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("#2 [DONE] Network")); assertTrue(out.contains("#1 [PLANNED] Printer"));
        assertTrue(out.contains("Xəta:")); assertTrue(out.contains("Naməlum əmr")); assertTrue(out.contains("Cəmi: 2"));
    }
    @Test void demoShowsEveryIndexAndRealCollision() {
        var bytes=new ByteArrayOutputStream(); Main.demo(new PrintStream(bytes,true,StandardCharsets.UTF_8));
        String out=bytes.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("chain=3")); assertTrue(out.contains("#2 [DONE]")); assertTrue(out.contains("BST"));
    }
    @Test void extraArgumentMustNotProcessTicket() throws Exception {
        var bytes=new ByteArrayOutputStream();
        Main.run(new StringReader("new X\ntriage normal\nprocess extra\nfind 1\n"),new PrintStream(bytes,true,StandardCharsets.UTF_8));
        String out=bytes.toString(StandardCharsets.UTF_8);
        assertFalse(out.contains("[DONE]")); assertTrue(out.contains("Xəta:"));
    }
}
