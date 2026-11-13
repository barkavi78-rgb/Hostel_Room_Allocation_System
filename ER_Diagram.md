
erDiagram
    STUDENT ||--o{ ROOM_ALLOCATION : has
    ROOM ||--o{ ROOM_ALLOCATION : assigned
    STUDENT ||--o{ FEE : pays

    STUDENT {
        int student_id PK
        string student_name
        string department
        int year_of_study
        string phone
        string email
    }

    ROOM {
        int room_id PK
        string room_number
        string room_type
        int capacity
        int occupied_count
        decimal monthly_fee
    }

    ROOM_ALLOCATION {
        int allocation_id PK
        int student_id FK
        int room_id FK
        date allocation_date
        date check_out_date
        string status
    }

    FEE {
        int fee_id PK
        int student_id FK
        decimal amount
        date payment_date
        string payment_status
    }
