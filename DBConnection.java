
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;

        try {
            String url = "jdbc:mysql://localhost:3306/hostel_management";
            String username = "root";
            String password = System.getenv("DB_PASSWORD");

            con = DriverManager.getConnection(url, username, password);

            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
    System.out.println("Connection Failed: " + e.getMessage());
}
        return con;
    }

    public static void main(String[] args) {
        getConnection();
    }
}
