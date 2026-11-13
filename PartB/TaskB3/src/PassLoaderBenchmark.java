import java.sql.*;
import java.util.*;

public class PassLoaderBenchmark {
    static List<String[]> generate(int n) {
        List<String[]> rows=new ArrayList<>(n);
        for(int i=0;i<n;i++) rows.add(new String[]{"P"+i,"REG"+i,"Student"+i,
                String.valueOf(i%30),String.valueOf(1000.0+(i%5000))});
        return rows;
    }
    static long statement(Connection c,List<String[]> rows) throws SQLException {
        boolean old=c.getAutoCommit();
        c.setAutoCommit(false);
        long start=System.nanoTime();
        try(Statement st=c.createStatement()) {
            for(String[] r:rows)
                st.executeUpdate("INSERT INTO bus_pass VALUES('"+r[0]+"','"+r[1]+"','"+r[2]+"',"+r[3]+","+r[4]+")");
            c.commit();
            return (System.nanoTime()-start)/1_000_000;
        } catch(SQLException | RuntimeException ex) {
            c.rollback();
            throw ex;
        } finally { c.setAutoCommit(old); }
    }
    static long prepared(Connection c,List<String[]> rows) throws SQLException {
        boolean old=c.getAutoCommit();
        c.setAutoCommit(false);
        long start=System.nanoTime();
        try(PreparedStatement ps=c.prepareStatement("INSERT INTO bus_pass VALUES(?,?,?,?,?)")) {
            for(String[] r:rows) {
                ps.setString(1,r[0]); ps.setString(2,r[1]); ps.setString(3,r[2]);
                ps.setInt(4,Integer.parseInt(r[3])); ps.setDouble(5,Double.parseDouble(r[4]));
                ps.executeUpdate();
            }
            c.commit();
            return (System.nanoTime()-start)/1_000_000;
        } catch(SQLException | RuntimeException ex) {
            c.rollback();
            throw ex;
        } finally { c.setAutoCommit(old); }
    }
    static long batch(Connection c,List<String[]> rows) throws SQLException {
        long start=System.nanoTime();
        PassLoader.loadAll(c,rows);
        return (System.nanoTime()-start)/1_000_000;
    }
    static void clear(Connection c) throws SQLException { try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");} }
    static void printVerification(Connection c, String label) throws SQLException {
        try(Statement st=c.createStatement();
            ResultSet rs=st.executeQuery("SELECT pass_id, reg_no, name, route_no, amount FROM bus_pass")) {
            ResultSetMetaData md=rs.getMetaData();
            System.out.println(label+" verification: columns="+md.getColumnCount());
            for(int i=1;i<=md.getColumnCount();i++)
                System.out.println("  "+md.getColumnName(i)+" -> "+md.getColumnTypeName(i));
            int count=0;
            while(rs.next()) {
                count++;
                System.out.println("  row: "+rs.getString("pass_id")+" | "+rs.getString("reg_no")
                    +" | "+rs.getString("name")+" | "+rs.getInt("route_no")+" | "+rs.getDouble("amount"));
            }
            System.out.println("  rows verified="+count);
        }
    }
    static void verifyTenEachApproach(Connection c) throws SQLException {
        List<String[]> rows=generate(10);
        clear(c);
        statement(c,rows);
        printVerification(c,"Statement");
        clear(c);
        prepared(c,rows);
        printVerification(c,"PreparedStatement");
        clear(c);
        PassLoader.loadAll(c,rows);
        printVerification(c,"PreparedStatement batch");
    }
    static void testAutoCommitFailure(Connection c) throws SQLException {
        clear(c);
        c.setAutoCommit(true);
        try(PreparedStatement ps=c.prepareStatement("INSERT INTO bus_pass VALUES(?,?,?,?,?)")) {
            for(int i=0;i<10;i++) {
                ps.setString(1,"AC"+i); ps.setString(2,"R"+i); ps.setString(3,"Auto"+i);
                ps.setInt(4,1); ps.setDouble(5,500.0);
                if(i==5) throw new SQLException("Forced failure after five committed rows");
                ps.executeUpdate();
            }
        } catch(SQLException ex) {
            System.out.println("Auto-commit forced failure: "+ex.getMessage());
        }
        try(Statement st=c.createStatement(); ResultSet rs=st.executeQuery("SELECT COUNT(*) FROM bus_pass")) {
            rs.next(); System.out.println("Rows remaining with auto-commit ON: "+rs.getInt(1));
        }
    }
    public static void main(String[] args) throws Exception {
        try(Connection c=DbConfig.connect()) {
            verifyTenEachApproach(c);
            for(int n:new int[]{1000,10000,100000}) {
                List<String[]> rows=generate(n);
                clear(c); long a=statement(c,rows);
                clear(c); long b=prepared(c,rows);
                clear(c); long d=batch(c,rows);
                System.out.printf("%d rows (one transaction each): Statement=%d ms, PreparedStatement=%d ms, PreparedStatement batch=%d ms%n",n,a,b,d);
            }
            // Auto-commit commits each prior statement independently.
            testAutoCommitFailure(c);
            // Transaction failure halfway: invalid numeric route causes rollback of the whole batch.
            clear(c);
            List<String[]> bad=generate(2000);
            bad.set(1000,new String[]{"PBAD","REGBAD","Invalid","not-int","99.0"});
            try { PassLoader.loadAll(c,bad); }
            catch(RuntimeException ex) { System.out.println("Forced failure: "+ex.getMessage()); }
            try(Statement s=c.createStatement(); ResultSet rs=s.executeQuery("SELECT COUNT(*) FROM bus_pass")) {
                rs.next(); System.out.println("Rows remaining after transactional failure: "+rs.getInt(1));
            }
        }
    }
}
