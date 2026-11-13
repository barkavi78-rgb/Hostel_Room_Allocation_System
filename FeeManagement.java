
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class FeeManagement {

    public static void displayFees() {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(
                "SELECT f.fee_id, s.student_name, " +
                "f.amount, f.payment_date, f.payment_status " +
                "FROM Fee f " +
                "JOIN Student s ON f.student_id = s.student_id"
            );

            System.out.println("FEE DETAILS");

            while (rs.next()) {
                System.out.println("Fee ID: " + rs.getInt("fee_id"));
                System.out.println("Student: " + rs.getString("student_name"));
                System.out.println("Amount: " + rs.getDouble("amount"));
                System.out.println("Payment Date: " + rs.getDate("payment_date"));
                System.out.println("Status: " + rs.getString("payment_status"));
                System.out.println("----------------------");
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addFee(Scanner sc) {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();

            System.out.print("Enter Amount: ");
            double amount = sc.nextDouble();

            System.out.print("Enter Payment Status (Paid/Pending): ");
            String status = sc.next();

            String sql = "INSERT INTO Fee " +
                "(student_id, amount, payment_date, payment_status) " +
                "VALUES (?, ?, CURDATE(), ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setDouble(2, amount);
            ps.setString(3, status);

            ps.executeUpdate();

            System.out.println("Fee Added Successfully!");

            con.close();
        } catch (Exception e) {
            System.out.println("Fee Addition Failed: " + e.getMessage());
        }
    }
}
