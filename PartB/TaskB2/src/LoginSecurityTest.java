import java.sql.*;

public class LoginSecurityTest {
    private static Connection connect() throws SQLException {
        String url=System.getenv().getOrDefault("CO5_DB_URL","jdbc:mysql://localhost:3306/co5_transport");
        String user=System.getenv().getOrDefault("CO5_DB_USER","root");
        String password=System.getenv().getOrDefault("CO5_DB_PASSWORD","");
        return DriverManager.getConnection(url,user,password);
    }
    static boolean unsafeLogin(Connection c,String id,String pwd) throws SQLException {
        String sql="SELECT * FROM test_users WHERE user_id='"+id+"' AND pwd='"+pwd+"'";
        try(Statement st=c.createStatement(); ResultSet rs=st.executeQuery(sql)){ return rs.next(); }
    }
    static boolean safeLogin(Connection c,String id,String pwd) throws SQLException {
        String sql="SELECT * FROM test_users WHERE user_id=? AND pwd=?";
        try(PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,id); ps.setString(2,pwd);
            try(ResultSet rs=ps.executeQuery()){ return rs.next(); }
        }
    }
    public static void main(String[] args) throws Exception {
        try(Connection c=connect()) {
            String attack="' OR '1'='1' -- ";
            System.out.println("Unsafe crafted login accepted: "+unsafeLogin(c,"nobody",attack));
            System.out.println("PreparedStatement crafted login accepted: "+safeLogin(c,"nobody",attack));
        }
    }
}
