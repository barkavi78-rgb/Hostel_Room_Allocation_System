
USE hostel_management;

-- 1. Display all students
SELECT * FROM Student;

-- 2. Display all rooms
SELECT * FROM Room;

-- 3. Display room allocation details
SELECT * FROM Room_Allocation;

-- 4. Display fee details
SELECT * FROM Fee;

-- 5. Count total students
SELECT COUNT(*) AS Total_Students
FROM Student;

-- 6. Display department-wise student count
SELECT department, COUNT(*) AS Student_Count
FROM Student
GROUP BY department;

-- 7. Display room occupancy
SELECT room_number, capacity, occupied_count
FROM Room;

-- 8. Display total fee amount
SELECT SUM(amount) AS Total_Fee
FROM Fee;

-- 9. Display payment status count
SELECT payment_status, COUNT(*) AS Total
FROM Fee
GROUP BY payment_status;

-- 10. Display student and room details
SELECT s.student_name, r.room_number, a.status
FROM Room_Allocation a
JOIN Student s ON a.student_id = s.student_id
JOIN Room r ON a.room_id = r.room_id;
