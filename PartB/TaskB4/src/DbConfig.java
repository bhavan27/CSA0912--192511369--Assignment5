import java.sql.*;
public class DbConfig {
    public static Connection connect() throws SQLException {
        String url=System.getenv().getOrDefault("CO5_DB_URL","jdbc:mysql://localhost:3306/co5_transport");
        String user=System.getenv().getOrDefault("CO5_DB_USER","root");
        String password=System.getenv().getOrDefault("CO5_DB_PASSWORD","");
        return DriverManager.getConnection(url,user,password);
    }
}
