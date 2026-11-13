
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class StudentManagement {

    public static void displayStudents() {

        try {
            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(
                "SELECT * FROM Student"
            );

            System.out.println("STUDENT DETAILS");
            System.out.println("----------------------------");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("student_name"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("Year: " + rs.getInt("year_of_study"));
                System.out.println("----------------------------");
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addStudent(Scanner sc) {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter Student Name: ");
            sc.nextLine();
            String name = sc.nextLine();

            System.out.print("Enter Department: ");
            String department = sc.nextLine();

            System.out.print("Enter Year: ");
            int year = sc.nextInt();

            System.out.print("Enter Phone: ");
            String phone = sc.next();

            System.out.print("Enter Email: ");
            String email = sc.next();

            String sql = "INSERT INTO Student " +
                "(student_name, department, year_of_study, phone, email) " +
                "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setInt(3, year);
            ps.setString(4, phone);
            ps.setString(5, email);

            ps.executeUpdate();

            System.out.println("Student Added Successfully!");

            

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
