
classDiagram
    class Student {
        +int student_id
        +String student_name
        +String department
        +int year_of_study
        +addStudent()
        +displayStudents()
    }

    class Room {
        +int room_id
        +String room_number
        +String room_type
        +int capacity
        +int occupied_count
        +addRoom()
        +displayRooms()
    }

    class RoomAllocation {
        +int allocation_id
        +Date allocation_date
        +Date check_out_date
        +String status
        +addAllocation()
        +checkoutStudent()
        +displayAllocations()
    }

    class Fee {
        +int fee_id
        +double amount
        +Date payment_date
        +String payment_status
        +addFee()
        +displayFees()
    }

    Student "1" --> "0..*" RoomAllocation
    Room "1" --> "0..*" RoomAllocation
    Student "1" --> "0..*" Fee
