import java.sql.*;

public class App{

    private final String url = "jdbc:postgresql://db-postgresql-ams3-47505-do-user-15053769-0.c.db.ondigitalocean.com:25060/defaultdb";
    private final String user = "doadmin";
    private final String password = "AVNS_w_VlxtdGYg344M9PbZC";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void main(String[] args) throws SQLException {
        Statement stmt = null;
        Connection conn = null;
        App app = new App();
        conn = app.connect();
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT customer_id, username, email FROM customer";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            //Retrieve by column name
            int id = rs.getInt("customer_id");
            String name = rs.getString("username");
            String email = rs.getString("email");

            System.out.print("ID: " + id);
            System.out.print(", Name: " + name);
            System.out.println(", Email: " + email);
        }
        rs.close();
        stmt.close();
        conn.close();
    }
}