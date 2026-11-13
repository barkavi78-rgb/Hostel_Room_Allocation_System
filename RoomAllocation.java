
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class RoomAllocation {

    // Display Room Allocations
    public static void displayAllocations() {

        try {
            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(
                "SELECT a.allocation_id, s.student_name, " +
                "r.room_number, a.allocation_date, " +
                "a.check_out_date, a.status " +
                "FROM Room_Allocation a " +
                "JOIN Student s ON a.student_id = s.student_id " +
                "JOIN Room r ON a.room_id = r.room_id"
            );

            System.out.println("ROOM ALLOCATION DETAILS");
            System.out.println("----------------------------");

            while (rs.next()) {
                System.out.println("Allocation ID: " + rs.getInt("allocation_id"));
                System.out.println("Student: " + rs.getString("student_name"));
                System.out.println("Room: " + rs.getString("room_number"));
                System.out.println("Allocation Date: " + rs.getDate("allocation_date"));
                System.out.println("Checkout Date: " + rs.getDate("check_out_date"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("----------------------------");
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Add Room Allocation
    public static void addAllocation(Scanner sc) {

        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();

            PreparedStatement checkStudent = con.prepareStatement(
                "SELECT * FROM Room_Allocation " +
                "WHERE student_id = ? AND status = 'Allocated'"
            );

            checkStudent.setInt(1, studentId);

            ResultSet studentResult = checkStudent.executeQuery();

            if (studentResult.next()) {
                System.out.println("Student Already Has a Room!");
                con.close();
                return;
            }

            System.out.print("Enter Room ID: ");
            int roomId = sc.nextInt();

            PreparedStatement checkRoom = con.prepareStatement(
                "SELECT capacity, occupied_count FROM Room WHERE room_id = ?"
            );

            checkRoom.setInt(1, roomId);

            ResultSet roomResult = checkRoom.executeQuery();

            if (roomResult.next()) {

                int capacity = roomResult.getInt("capacity");
                int occupied = roomResult.getInt("occupied_count");

                if (occupied >= capacity) {
                    System.out.println("Room is Full!");
                    con.close();
                    return;
                }

            } else {
                System.out.println("Invalid Room ID!");
                con.close();
                return;
            }

            String sql = "INSERT INTO Room_Allocation " +
                "(student_id, room_id, allocation_date, status) " +
                "VALUES (?, ?, CURDATE(), 'Allocated')";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setInt(2, roomId);

            ps.executeUpdate();

            PreparedStatement updateRoom = con.prepareStatement(
                "UPDATE Room SET occupied_count = occupied_count + 1 " +
                "WHERE room_id = ?"
            );

            updateRoom.setInt(1, roomId);
            updateRoom.executeUpdate();

            System.out.println("Room Allocated Successfully!");

            con.close();

        } catch (Exception e) {
            System.out.println("Allocation Failed: " + e.getMessage());
        }
    }

    // Room Checkout
    public static void checkoutStudent(Scanner sc) {

        try {
            Connection con = DBConnection.getConnection();
            con.setAutoCommit(false);

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();

            PreparedStatement check = con.prepareStatement(
                "SELECT room_id FROM Room_Allocation " +
                "WHERE student_id = ? AND status = 'Allocated'"
            );

            check.setInt(1, studentId);

            ResultSet rs = check.executeQuery();

            if (rs.next()) {

                int roomId = rs.getInt("room_id");

                PreparedStatement updateAllocation = con.prepareStatement(
                    "UPDATE Room_Allocation " +
                    "SET check_out_date = CURDATE(), status = 'Checked Out' " +
                    "WHERE student_id = ? AND status = 'Allocated'"
                );

                updateAllocation.setInt(1, studentId);
                updateAllocation.executeUpdate();

                PreparedStatement updateRoom = con.prepareStatement(
                    "UPDATE Room SET occupied_count = occupied_count - 1 " +
                    "WHERE room_id = ? AND occupied_count > 0"
                );

                updateRoom.setInt(1, roomId);
                updateRoom.executeUpdate();

                con.commit();

                System.out.println("Student Checked Out Successfully!");

            } else {
                con.rollback();
                System.out.println("No Active Room Allocation Found!");
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Checkout Failed: " + e.getMessage());
        }
    }
}
