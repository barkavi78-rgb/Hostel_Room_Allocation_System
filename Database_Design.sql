
CREATE DATABASE hostel_management;

USE hostel_management;

-- Student Table
CREATE TABLE Student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    year_of_study INT,
    phone VARCHAR(15),
    email VARCHAR(100)
);

-- Room Table
CREATE TABLE Room (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) UNIQUE,
    room_type VARCHAR(30),
    capacity INT,
    occupied_count INT DEFAULT 0,
    monthly_fee DECIMAL(10,2)
);

-- Room Allocation Table
CREATE TABLE Room_Allocation (
    allocation_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    room_id INT,
    allocation_date DATE,
    check_out_date DATE,
    status VARCHAR(20),
    FOREIGN KEY (student_id) REFERENCES Student(student_id),
    FOREIGN KEY (room_id) REFERENCES Room(room_id)
);

-- Fee Table
CREATE TABLE Fee (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    amount DECIMAL(10,2),
    payment_date DATE,
    payment_status VARCHAR(20),
    FOREIGN KEY (student_id) REFERENCES Student(student_id)
);
