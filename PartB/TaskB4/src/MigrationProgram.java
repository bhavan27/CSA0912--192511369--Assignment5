import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class MigrationProgram {
    public static void main(String[] args) throws Exception {
        Path file=Paths.get(args.length>0?args[0]:"../input/legacy-passes.csv");
        Path outputDir=Paths.get(args.length>1?args[1]:"../output");
        Files.createDirectories(outputDir);
        List<String[]> valid=new ArrayList<>();
        List<String> rejected=new ArrayList<>();
        int lineNo=0;
        try(BufferedReader br=Files.newBufferedReader(file)) {
            String line;
            while((line=br.readLine())!=null) {
                lineNo++;
                String[] f=line.split(",",-1);
                try {
                    if(f.length!=5) throw new IllegalArgumentException("expected 5 fields");
                    Integer.parseInt(f[3]); Double.parseDouble(f[4]);
                    if(f[0].isBlank()||f[1].isBlank()||f[2].isBlank()) throw new IllegalArgumentException("required field empty");
                    valid.add(f);
                } catch(Exception e) { rejected.add("line "+lineNo+": "+e.getMessage()+" | "+line); }
            }
        }
        Path rejectionFile=outputDir.resolve("migration-rejections.txt");
        Files.write(rejectionFile,rejected);
        try(Connection con=DbConfig.connect()) {
            int inserted=PassLoader.loadAll(con,valid);
            System.out.println("Valid records loaded: "+inserted);
            System.out.println("Rejected records: "+rejected.size());
            System.out.println("Rejection report: "+rejectionFile.toAbsolutePath());
            DatabaseMetaData md=con.getMetaData();
            System.out.println("Database product: "+md.getDatabaseProductName()+" "+md.getDatabaseProductVersion());
            System.out.println("JDBC driver: "+md.getDriverName()+" "+md.getDriverVersion());
            try(ResultSet tables=md.getTables(con.getCatalog(),null,"%",new String[]{"TABLE"})) {
                System.out.println("Tables:");
                while(tables.next()) System.out.println(" - "+tables.getString("TABLE_NAME"));
            }
        }
    }
}
