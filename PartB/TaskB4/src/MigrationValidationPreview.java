import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Executes the CSV validation stage without connecting to a database. */
public class MigrationValidationPreview {
    public static void main(String[] args) throws Exception {
        Path input=Paths.get(args.length>0?args[0]:"../input/legacy-passes.csv");
        List<String> rejected=new ArrayList<>();
        int valid=0, lineNo=0;
        try(BufferedReader br=Files.newBufferedReader(input)) {
            String line;
            while((line=br.readLine())!=null) {
                lineNo++;
                String[] f=line.split(",",-1);
                String reason=null;
                if(f.length!=5) reason="expected 5 fields";
                else {
                    try { Integer.parseInt(f[3]); Double.parseDouble(f[4]); }
                    catch(NumberFormatException ex) { reason="route/amount must be numeric"; }
                }
                if(reason==null) { valid++; System.out.println("VALID line "+lineNo+": "+f[0]); }
                else {
                    rejected.add("line "+lineNo+": "+reason+" | "+line);
                    System.out.println("REJECTED line "+lineNo+": "+reason);
                }
            }
        }
        Files.write(Paths.get(args.length>1?args[1]:"../output/migration-rejections-preview.txt"),rejected);
        System.out.println("Valid records: "+valid);
        System.out.println("Rejected records: "+rejected.size());
        System.out.println("Validation stage completed; database loading is a separate step.");
    }
}
