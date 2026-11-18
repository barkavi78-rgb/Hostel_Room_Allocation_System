
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.nio.charset.StandardCharsets;

public class StudentAPI {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
            new InetSocketAddress(8080), 0
        );

        // DASHBOARD API
        server.createContext("/api/dashboard", exchange -> {

            String response;

            try (Connection con = DBConnection.getConnection()) {

                Statement stmt = con.createStatement();

                ResultSet students = stmt.executeQuery(
                    "SELECT COUNT(*) FROM Student"
                );
                students.next();
                int totalStudents = students.getInt(1);

                ResultSet rooms = stmt.executeQuery(
                    "SELECT COUNT(*) FROM Room"
                );
                rooms.next();
                int totalRooms = rooms.getInt(1);

                ResultSet occupied = stmt.executeQuery(
                    "SELECT COUNT(DISTINCT room_id) FROM Room_Allocation WHERE status = 'Active'"
                );
                occupied.next();
                int occupiedRooms = occupied.getInt(1);

                int availableRooms = totalRooms - occupiedRooms;

                response = "{\"students\":" + totalStudents
                    + ",\"rooms\":" + totalRooms
                    + ",\"occupied\":" + occupiedRooms
                    + ",\"available\":" + availableRooms + "}";

            } catch (Exception e) {
                response = "{\"error\":\"Database error\"}";
            }

            sendResponse(exchange, response);
        });

        // VIEW STUDENTS API
        server.createContext("/api/students", exchange -> {

            StringBuilder response = new StringBuilder("[");
            boolean first = true;

            try (Connection con = DBConnection.getConnection()) {

                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery(
                    "SELECT student_id, student_name, department, year_of_study, phone, email FROM Student"
                );

                while (rs.next()) {

                    if (!first) response.append(",");
                    first = false;

                    response.append("{")
                        .append("\"id\":").append(rs.getInt("student_id")).append(",")
                        .append("\"name\":\"").append(escape(rs.getString("student_name"))).append("\",")
                        .append("\"department\":\"").append(escape(rs.getString("department"))).append("\",")
                        .append("\"year\":").append(rs.getInt("year_of_study")).append(",")
                        .append("\"phone\":\"").append(escape(rs.getString("phone"))).append("\",")
                        .append("\"email\":\"").append(escape(rs.getString("email"))).append("\"")
                        .append("}");
                }

                response.append("]");

            } catch (Exception e) {
                response = new StringBuilder("{\"error\":\"Unable to load students\"}");
            }

            sendResponse(exchange, response.toString());
        });

        // ADD STUDENT API
        server.createContext("/api/add-student", exchange -> {

            String response;

            try {

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
                );

                StringBuilder body = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    body.append(line);
                }

                String name = getValue(body.toString(), "name");
                String department = getValue(body.toString(), "department");
                int year = Integer.parseInt(getValue(body.toString(), "year"));
                String phone = getValue(body.toString(), "phone");
                String email = getValue(body.toString(), "email");

                try (Connection con = DBConnection.getConnection()) {

                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Student (student_name, department, year_of_study, phone, email) VALUES (?, ?, ?, ?, ?)"
                    );

                    ps.setString(1, name);
                    ps.setString(2, department);
                    ps.setInt(3, year);
                    ps.setString(4, phone);
                    ps.setString(5, email);

                    ps.executeUpdate();

                    response = "{\"message\":\"Student added successfully\"}";
                }

            } catch (Exception e) {
                response = "{\"error\":\"Unable to add student\"}";
            }

            sendResponse(exchange, response);
        });

        // VIEW ROOMS API
        server.createContext("/api/rooms", exchange -> {

            StringBuilder response = new StringBuilder("[");
            boolean first = true;

            try (Connection con = DBConnection.getConnection()) {

                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery(
                    "SELECT room_id, room_number, room_type, capacity, monthly_fee FROM Room"
                );

                while (rs.next()) {

                    if (!first) response.append(",");
                    first = false;

                    response.append("{")
                        .append("\"id\":").append(rs.getInt("room_id")).append(",")
                        .append("\"number\":\"").append(escape(rs.getString("room_number"))).append("\",")
                        .append("\"type\":\"").append(escape(rs.getString("room_type"))).append("\",")
                        .append("\"capacity\":").append(rs.getInt("capacity")).append(",")
                        .append("\"fee\":").append(rs.getDouble("monthly_fee"))
                        .append("}");
                }

                response.append("]");

            } catch (Exception e) {
                response = new StringBuilder("{\"error\":\"Unable to load rooms\"}");
            }

            sendResponse(exchange, response.toString());
        });

        // VIEW ALLOCATIONS API
        server.createContext("/api/allocations", exchange -> {

            StringBuilder response = new StringBuilder("[");
            boolean first = true;

            try (Connection con = DBConnection.getConnection()) {

                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery(
                    "SELECT a.allocation_id, s.student_name, " +
                    "r.room_number, a.allocation_date, " +
                    "a.check_out_date, a.status " +
                    "FROM Room_Allocation a " +
                    "JOIN Student s ON a.student_id = s.student_id " +
                    "JOIN Room r ON a.room_id = r.room_id"
                );

                while (rs.next()) {

                    if (!first) response.append(",");
                    first = false;

                    response.append("{")
                        .append("\"id\":").append(rs.getInt("allocation_id")).append(",")
                        .append("\"student\":\"").append(escape(rs.getString("student_name"))).append("\",")
                        .append("\"room\":\"").append(escape(rs.getString("room_number"))).append("\",")
                        .append("\"allocationDate\":\"")
                        .append(rs.getDate("allocation_date") == null ? "" : rs.getDate("allocation_date").toString()).append("\",")
                        .append("\"checkoutDate\":\"")
                        .append(rs.getDate("check_out_date") == null ? "" : rs.getDate("check_out_date").toString()).append("\",")
                        .append("\"status\":\"").append(escape(rs.getString("status"))).append("\"")
                        .append("}");
                }

                response.append("]");

            } catch (Exception e) {
                response = new StringBuilder("{\"error\":\"Unable to load allocations\"}");
            }

            sendResponse(exchange, response.toString());
        });

        // ADD ALLOCATION API
        server.createContext("/api/add-allocation", exchange -> {

            String response;

            try {

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
                );

                StringBuilder body = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    body.append(line);
                }

                int studentId = Integer.parseInt(
                    getValue(body.toString(), "studentId")
                );

                int roomId = Integer.parseInt(
                    getValue(body.toString(), "roomId")
                );

                String allocationDate = getValue(body.toString(), "allocationDate");
                String checkoutDate = getValue(body.toString(), "checkoutDate");
                String status = getValue(body.toString(), "status");

                try (Connection con = DBConnection.getConnection()) {

                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Room_Allocation " +
                        "(student_id, room_id, allocation_date, check_out_date, status) " +
                        "VALUES (?, ?, ?, ?, ?)"
                    );

                    ps.setInt(1, studentId);
                    ps.setInt(2, roomId);
                    ps.setDate(3, java.sql.Date.valueOf(allocationDate));

                    if (checkoutDate == null || checkoutDate.trim().isEmpty()) {
                        ps.setNull(4, java.sql.Types.DATE);
                    } else {
                        ps.setDate(4, java.sql.Date.valueOf(checkoutDate));
                    }

                    ps.setString(5, status);

                    ps.executeUpdate();

                    response = "{\"message\":\"Allocation added successfully\"}";
                }

            } catch (Exception e) {
                response = "{\"error\":\"Unable to add allocation\"}";
            }

            sendResponse(exchange, response);
        });

        // VIEW FEES API
        server.createContext("/api/fees", exchange -> {

            StringBuilder response = new StringBuilder("[");
            boolean first = true;

            try (Connection con = DBConnection.getConnection()) {

                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery(
                    "SELECT f.fee_id, s.student_name, " +
                    "f.amount, f.payment_date, f.payment_status " +
                    "FROM Fee f " +
                    "JOIN Student s ON f.student_id = s.student_id"
                );

                while (rs.next()) {

                    if (!first) response.append(",");
                    first = false;

                    response.append("{")
                        .append("\"id\":").append(rs.getInt("fee_id")).append(",")
                        .append("\"student\":\"").append(escape(rs.getString("student_name"))).append("\",")
                        .append("\"amount\":").append(rs.getDouble("amount")).append(",")
                        .append("\"date\":\"")
                        .append(rs.getDate("payment_date") == null ? "" : rs.getDate("payment_date").toString()).append("\",")
                        .append("\"status\":\"").append(escape(rs.getString("payment_status"))).append("\"")
                        .append("}");
                }

                response.append("]");

            } catch (Exception e) {
                response = new StringBuilder("{\"error\":\"Unable to load fees\"}");
            }

            sendResponse(exchange, response.toString());
        });

        // ADD FEE API
        server.createContext("/api/add-fee", exchange -> {

            String response;

            try {

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
                );

                StringBuilder body = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    body.append(line);
                }

                int studentId = Integer.parseInt(
                    getValue(body.toString(), "studentId")
                );

                double amount = Double.parseDouble(
                    getValue(body.toString(), "amount")
                );

                String status = getValue(body.toString(), "status");

                try (Connection con = DBConnection.getConnection()) {

                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Fee (student_id, amount, payment_date, payment_status) " +
                        "VALUES (?, ?, CURDATE(), ?)"
                    );

                    ps.setInt(1, studentId);
                    ps.setDouble(2, amount);
                    ps.setString(3, status);

                    ps.executeUpdate();

                    response = "{\"message\":\"Fee added successfully\"}";
                }

            } catch (Exception e) {
                response = "{\"error\":\"Unable to add fee\"}";
            }

            sendResponse(exchange, response);
        });

        server.start();

        System.out.println(
            "Student API running at http://localhost:8080"
        );
    }

    // READ FORM VALUES
    private static String getValue(String body, String key) throws Exception {

        for (String pair : body.split("&")) {

            String[] parts = pair.split("=", 2);

            if (parts.length == 2 &&
                parts[0].equals(key)) {

                return URLDecoder.decode(parts[1], "UTF-8");
            }
        }

        throw new Exception("Missing field: " + key);
    }

    // ESCAPE JSON VALUES
    private static String escape(String value) {

        if (value == null) return "";

        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    // SEND RESPONSE
    private static void sendResponse(
        HttpExchange exchange,
        String response
    ) throws java.io.IOException {

        exchange.getResponseHeaders().set(
            "Content-Type", "application/json; charset=UTF-8"
        );

        exchange.getResponseHeaders().set(
            "Access-Control-Allow-Origin", "*"
        );

        byte[] bytes = response.getBytes("UTF-8");

        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
