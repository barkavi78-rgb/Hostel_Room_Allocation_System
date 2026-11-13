
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class RoomManagement {

    public static void displayRooms() {

        try {
            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Room");

            System.out.println("ROOM DETAILS");
            System.out.println("----------------------");

            while (rs.next()) {
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Room Number: " + rs.getString("room_number"));
                System.out.println("Room Type: " + rs.getString("room_type"));
                System.out.println("Capacity: " + rs.getInt("capacity"));
                System.out.println("Monthly Fee: " + rs.getDouble("monthly_fee"));
                
                System.out.println("----------------------");
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addRoom(Scanner sc) {

        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter Room Number: ");
            String number = sc.nextLine();

            System.out.print("Enter Room Type: ");
            String type = sc.nextLine();

            System.out.print("Enter Capacity: ");
            int capacity = sc.nextInt();

            System.out.print("Enter Monthly Fee: ");
            double fee = sc.nextDouble();

            String sql = "INSERT INTO Room " +
                "(room_number, room_type, capacity, monthly_fee) " +
                "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, number);
            ps.setString(2, type);
            ps.setInt(3, capacity);
            ps.setDouble(4, fee);

            ps.executeUpdate();

            System.out.println("Room Added Successfully!");

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
