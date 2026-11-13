import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DamagedPassFileTest {
    public static void main(String[] args) throws Exception {
        Path file=Paths.get(args.length>0?args[0]:"../input/damaged-passes.csv");
        int lineNo=0, accepted=0, rejected=0;
        try(BufferedReader br=Files.newBufferedReader(file)) {
            String line;
            while((line=br.readLine())!=null) {
                lineNo++;
                String[] f=line.split(",",-1);
                String reason=null;
                if(line.isBlank()) reason="empty line";
                else if(f.length!=5) reason="wrong number of fields";
                else {
                    try { Integer.parseInt(f[3]); Double.parseDouble(f[4]); }
                    catch(NumberFormatException e){ reason="non-numeric route/amount"; }
                }
                if(reason==null){ accepted++; System.out.println("Line "+lineNo+": ACCEPT "+Arrays.toString(f));}
                else { rejected++; System.out.println("Line "+lineNo+": REJECT ("+reason+")");}
            }
        }
        System.out.println("Accepted="+accepted+", rejected="+rejected);
    }
}
