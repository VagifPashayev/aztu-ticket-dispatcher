package az.edu.aztu.ticketdesk;

import az.edu.aztu.ticketdesk.structures.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

/** Small educational measurement harness; see docs/benchmark-method.md for limitations. */
public final class Benchmarks {
    private static volatile long blackhole;
    private static final String[] STRUCTURES={"FixedArray","DynamicArray","SinglyList","DoublyList"};
    private static final String[] OPERATIONS={"grow_append","append","insert_front","remove_front","remove_back","search_hit","search_miss"};
    private record Sample(String structure,String operation,int n,int round,int operations,long nanos,long checksum) {
        double nsPerOp() { return (double)nanos/operations; }
    }
    private Benchmarks() { }
    private static Sequence<Integer> sequence(String name,int capacity) {
        return switch(name) {
            case "FixedArray" -> new FixedArray<>(capacity);
            case "DynamicArray" -> new DynamicArray<>(capacity);
            case "SinglyList" -> new SinglyLinkedList<>();
            case "DoublyList" -> new DoublyLinkedList<>();
            default -> throw new IllegalArgumentException(name);
        };
    }
    private static Sample measure(String name,String operation,int n,int round,int batch) {
        boolean grow=operation.equals("grow_append");
        Integer[] values=new Integer[n+batch];
        for(int i=0;i<values.length;i++) values[i]=i;
        Sequence<Integer> data=sequence(name,grow && name.equals("DynamicArray")?1:n+batch);
        if(!grow) for(int i=0;i<n;i++) data.add(values[i]);
        Integer[] queries=new Integer[batch]; Random random=new Random(20262L+n+round);
        for(int i=0;i<batch;i++) queries[i]=operation.equals("search_miss")?-(i+1):values[random.nextInt(n)];
        int count=grow?n:batch;
        long sum=0,start=System.nanoTime();
        switch(operation) {
            case "grow_append" -> { for(int i=0;i<count;i++) data.add(values[i]); }
            case "append" -> { for(int i=0;i<count;i++) data.add(values[n+i]); }
            case "insert_front" -> { for(int i=0;i<count;i++) data.insert(0,values[n+i]); }
            case "remove_front" -> { for(int i=0;i<count;i++) sum+=data.remove(0); }
            case "remove_back" -> { for(int i=0;i<count;i++) sum+=data.remove(data.size()-1); }
            case "search_hit", "search_miss" -> { for(Integer query:queries) sum+=data.indexOf(query); }
            default -> throw new IllegalArgumentException(operation);
        }
        long elapsed=System.nanoTime()-start;
        int expectedSize=switch(operation) {
            case "grow_append" -> n;
            case "append", "insert_front" -> n+batch;
            case "remove_front", "remove_back" -> n-batch;
            default -> n;
        };
        if(data.size()!=expectedSize) throw new IllegalStateException("Incorrect benchmark state");
        long expectedSum=0;
        switch(operation) {
            case "remove_front" -> { for(int i=0;i<batch;i++) expectedSum+=i; }
            case "remove_back" -> { for(int i=0;i<batch;i++) expectedSum+=n-1-i; }
            case "search_hit" -> { for(Integer query:queries) expectedSum+=query; }
            case "search_miss" -> expectedSum=-batch;
            case "grow_append" -> { sum=(long)data.get(0)+data.get(data.size()-1)+data.size(); expectedSum=2L*n-1; }
            case "append" -> { sum=(long)data.get(0)+data.get(data.size()-1)+data.size(); expectedSum=2L*(n+batch)-1; }
            case "insert_front" -> { sum=(long)data.get(0)+data.get(data.size()-1)+data.size(); expectedSum=3L*n+2L*batch-2; }
            default -> throw new IllegalArgumentException(operation);
        }
        if(sum!=expectedSum) throw new IllegalStateException("Incorrect operation result");
        blackhole=sum;
        return new Sample(name,operation,n,round,count,elapsed,sum);
    }
    public static void run(Path output,boolean quick,PrintStream log) throws IOException {
        Files.createDirectories(output);
        int[] sizes=quick?new int[]{64,256}:new int[]{1000,10000,50000};
        int rounds=quick?3:5,warmups=quick?1:2,batch=quick?16:128;
        var samples=new DynamicArray<Sample>();
        log.printf("Benchmark: %d warm-up + %d measured rounds; batch=%d%n",warmups,rounds,batch);
        for(int n:sizes) {
            for(String operation:OPERATIONS) {
                for(int round=-warmups;round<rounds;round++) {
                    for(int offset=0;offset<STRUCTURES.length;offset++) {
                        String name=STRUCTURES[Math.floorMod(offset+round,STRUCTURES.length)];
                        Sample sample=measure(name,operation,n,round,batch);
                        if(round>=0) samples.add(sample);
                    }
                }
            }
        }
        var raw=new StringBuilder("structure,operation,n,round,operations,elapsed_ns,ns_per_op,checksum\n");
        for(int i=0;i<samples.size();i++) {
            Sample s=samples.get(i);
            raw.append(String.format(Locale.ROOT,"%s,%s,%d,%d,%d,%d,%.3f,%d%n",s.structure(),s.operation(),s.n(),s.round(),s.operations(),s.nanos(),s.nsPerOp(),s.checksum()));
        }
        Files.writeString(output.resolve("sequence-rounds.csv"),raw,StandardCharsets.UTF_8);
        var summary=new StringBuilder("structure,operation,n,rounds,median_ns_per_op,min_ns_per_op,max_ns_per_op\n");
        log.println("Median ns/op (local educational measurements):");
        for(int n:sizes) for(String operation:OPERATIONS) {
            StringBuilder line=new StringBuilder(String.format(Locale.ROOT,"n=%-5d %-13s",n,operation));
            for(String name:STRUCTURES) {
                double[] times=new double[rounds]; int used=0;
                for(int i=0;i<samples.size();i++) {
                    Sample s=samples.get(i);
                    if(s.n()==n && s.operation().equals(operation) && s.structure().equals(name)) times[used++]=s.nsPerOp();
                }
                if(used!=rounds) throw new IllegalStateException("Missing benchmark round");
                Arrays.sort(times); double median=times[rounds/2];
                summary.append(String.format(Locale.ROOT,"%s,%s,%d,%d,%.3f,%.3f,%.3f%n",name,operation,n,rounds,median,times[0],times[rounds-1]));
                line.append(String.format(Locale.ROOT," | %s=%.1f",name,median));
            }
            log.println(line);
        }
        Files.writeString(output.resolve("sequence-summary.csv"),summary,StandardCharsets.UTF_8);
        measureBst(output,quick,rounds,warmups,log);
        String environment="generated_utc="+Instant.now()+"\njava.version="+System.getProperty("java.version")+"\njava.vm.name="+System.getProperty("java.vm.name")+"\nos.name="+System.getProperty("os.name")+"\nos.arch="+System.getProperty("os.arch")+"\navailable.processors="+Runtime.getRuntime().availableProcessors()+"\nseed=20262+n+round\nmode="+(quick?"quick-test":"assignment")+"\nrounds="+rounds+"\nwarmups="+warmups+"\nbatch="+batch+"\n";
        Files.writeString(output.resolve("environment.txt"),environment,StandardCharsets.UTF_8);
        log.println("CSV saved: " + output + " | checksum=" + blackhole);
    }
    private static void measureBst(Path output,boolean quick,int rounds,int warmups,PrintStream log) throws IOException {
        var csv=new StringBuilder("order,n,round,build_ns,search_ns_per_op,height,mean_search_comparisons,checksum\n");
        int[] sizes=quick?new int[]{64,256}:new int[]{1000,3000,6000};
        log.println("BST: height counts nodes (empty=0); ordinary unbalanced tree.");
        for(int n:sizes) for(String order:new String[]{"sorted","random"}) {
            for(int round=-warmups;round<rounds;round++) {
                Integer[] keys=new Integer[n]; for(int i=0;i<n;i++) keys[i]=i;
                Random random=new Random(20262L+n+round);
                if(order.equals("random")) for(int i=n-1;i>0;i--) { int j=random.nextInt(i+1); Integer old=keys[i]; keys[i]=keys[j]; keys[j]=old; }
                var tree=new BinarySearchTree<Integer>();
                long start=System.nanoTime(); for(Integer key:keys) tree.put(key,key); long build=System.nanoTime()-start;
                int[] queries=new int[128]; Random queryRandom=new Random(20262L+n);
                long expected=0; for(int i=0;i<queries.length;i++) { queries[i]=queryRandom.nextInt(n); expected+=queries[i]; }
                long sum=0; start=System.nanoTime(); for(int query:queries) sum+=tree.get(query); long search=System.nanoTime()-start;
                blackhole=sum;
                int height=tree.height();
                if(sum!=expected || tree.size()!=n || (order.equals("sorted") && height!=n)) throw new IllegalStateException("Invalid BST experiment");
                long comparisons=0; for(int query:queries) comparisons+=tree.searchComparisons(query);
                if(round>=0) csv.append(String.format(Locale.ROOT,"%s,%d,%d,%d,%.3f,%d,%.3f,%d%n",order,n,round,build,(double)search/queries.length,height,(double)comparisons/queries.length,sum));
                if(round==rounds-1) log.printf(Locale.ROOT,"BST n=%d %-6s height=%d | mean comparisons=%.2f%n",n,order,height,(double)comparisons/queries.length);
            }
        }
        Files.writeString(output.resolve("bst-rounds.csv"),csv,StandardCharsets.UTF_8);
    }
}
