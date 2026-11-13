import java.sql.*;
import java.util.List;

public class PassLoader {
    /** Loads passes in one transaction, executing batches of 1,000. */
    public static int loadAll(Connection con, List<String[]> passes) throws SQLException {
        String sql="INSERT INTO bus_pass(pass_id, reg_no, name, route_no, amount) VALUES (?, ?, ?, ?, ?)";
        boolean oldAutoCommit=con.getAutoCommit();
        con.setAutoCommit(false);
        try(PreparedStatement ps=con.prepareStatement(sql)) {
            int n=0, pending=0;
            for(String[] p:passes) {
                ps.setString(1,p[0]);
                ps.setString(2,p[1]);
                ps.setString(3,p[2]);
                ps.setInt(4,Integer.parseInt(p[3]));
                ps.setDouble(5,Double.parseDouble(p[4]));
                ps.addBatch();
                n++; pending++;
                if(pending==1000) { ps.executeBatch(); pending=0; }
            }
            if(pending>0) ps.executeBatch();
            con.commit();
            return n;
        } catch(SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }
}
