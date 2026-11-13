import java.io.*;
import java.nio.file.*;
import java.util.*;

public class B1Runner {
    private static void createLog(Path p, long bytes) throws IOException {
        byte[] line = "attendance-record-1234567890\n".getBytes();
        try (OutputStream out = Files.newOutputStream(p)) {
            long written=0;
            while (written < bytes) {
                int n=(int)Math.min(line.length, bytes-written);
                out.write(line,0,n); written+=n;
            }
        }
    }
    private static long timedCount(Path p, boolean buffered) throws Exception {
        long start=System.nanoTime();
        long count=buffered?BufferedLineCounter.countBuffered(p):BufferedLineCounter.countUnbuffered(p);
        return (System.nanoTime()-start)/1_000_000;
    }
    private static void writePassFile(Path p, int n) throws IOException {
        try(BufferedWriter w=Files.newBufferedWriter(p)) {
            for(int i=0;i<n;i++) w.write("P"+i+",REG"+i+",Student"+i+","+(i%20)+","+(1000+i)+"\n");
        }
    }
    public static void main(String[] args) throws Exception {
        Path input=Paths.get(args.length>0?args[0]:"../input/passes.csv");
        Path output=Paths.get(args.length>1?args[1]:"../output");
        Files.createDirectories(output);
        Path workingFile=output.resolve("passes-working.csv");
        Files.copy(input,workingFile,StandardCopyOption.REPLACE_EXISTING);
        PassStore store=new PassStore(workingFile);
        System.out.println("B1 – PassStore validation");
        store.add("P9001","192319001","Kavya",12,4500);
        System.out.println("Find P9001: "+Arrays.toString(store.find("P9001")));
        System.out.println("Find P-NOT-FOUND: "+Arrays.toString(store.find("P-NOT-FOUND")));
        System.out.println("Route 12 records: "+store.listByRoute(12).size());

        Path damaged=output.resolve("damaged-test.csv");
        Files.writeString(damaged,"P1,R1,Name,3,100.0\nBAD,LINE\nP2,R2,Name,4,not-a-number\n\nP3,R3,Valid,5,200.0\n");
        PassStore damagedStore=new PassStore(damaged);
        System.out.println("Damaged-file find P3: "+Arrays.toString(damagedStore.find("P3")));
        System.out.println("Damaged-file find P2: "+Arrays.toString(damagedStore.find("P2")));

        System.out.println("\nLine counter measurements (milliseconds; this machine):");
        long[] sizes={1L,10L,50L};
        for(long mb:sizes) {
            Path log=output.resolve("attendance-"+mb+"MB.log");
            createLog(log,mb*1024*1024);
            long u=timedCount(log,false);
            long b=timedCount(log,true);
            System.out.println(mb+" MB: unbuffered="+u+" ms, buffered="+b+" ms");
        }
        System.out.println("\nLookup growth measurements (milliseconds; target is last ID):");
        for(int n:new int[]{1000,10000,100000}) {
            Path f=output.resolve("passes-"+n+".csv"); writePassFile(f,n);
            PassStore ps=new PassStore(f);
            long start=System.nanoTime(); String[] found=ps.find("P"+(n-1));
            long ms=(System.nanoTime()-start)/1_000_000;
            System.out.println(n+" records: "+ms+" ms; found="+(found!=null));
        }
        System.out.println("\nPASS: B1 experiments completed.");
    }
}
