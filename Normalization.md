
# DATABASE NORMALIZATION

## 1. First Normal Form (1NF)
- Each table contains unique records.
- Each column contains atomic values.
- No multiple values are stored in a single cell.

## 2. Second Normal Form (2NF)
- The database follows 1NF.
- Every non-key attribute depends on the complete primary key.
- Each table has a primary key.

## 3. Third Normal Form (3NF)
- The database follows 2NF.
- No non-key attribute depends on another non-key attribute.
- Student, Room, Room Allocation and Fee details are maintained separately.

## BENEFITS
- Reduces data duplication.
- Improves data consistency.
- Makes database maintenance easier.
