package az.edu.aztu.ticketdesk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class BenchmarksTest {
    @TempDir Path temp;
    @Test void exportsMeasuredRoundsSummariesAndSortedBstEvidence() throws Exception {
        Benchmarks.run(temp,true,new PrintStream(OutputStream.nullOutputStream()));
        var rows=Files.readAllLines(temp.resolve("sequence-rounds.csv"));
        assertEquals(169,rows.size());
        for(String row:rows.subList(1,rows.size())) {
            String[] f=row.split(","); assertEquals(8,f.length); assertTrue(Long.parseLong(f[5])>0); assertTrue(Double.parseDouble(f[6])>0);
            assertTrue(Long.parseLong(f[4])>0);
        }
        assertEquals(57,Files.readAllLines(temp.resolve("sequence-summary.csv")).size());
        var bst=Files.readAllLines(temp.resolve("bst-rounds.csv")); assertEquals(13,bst.size());
        for(String row:bst.subList(1,bst.size())) {
            String[] f=row.split(",");
            if(f[0].equals("sorted")) assertEquals(Integer.parseInt(f[1]),Integer.parseInt(f[5]));
            assertTrue(Double.parseDouble(f[6])>=1);
        }
        assertTrue(Files.readString(temp.resolve("environment.txt")).contains("java.version="));
    }
}
