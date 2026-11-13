
import mysql.connector
import pandas as pd
from getpass import getpass

password = getpass("Enter MySQL password: ")

con = mysql.connector.connect(
    host="localhost",
    user="root",
    password=password,
    database="hostel_management"
)

query = "SELECT * FROM Student"

df = pd.read_sql(query, con)

print("\nSTUDENT DETAILS")
print(df)

print("\nTOTAL NUMBER OF STUDENTS:")
print(len(df))

print("\nDEPARTMENT-WISE STUDENT COUNT:")
print(df["department"].value_counts())



query = "SELECT * FROM Room"

room_df = pd.read_sql(query, con)

print("\nROOM DETAILS")
print(room_df)

print("\nTOTAL NUMBER OF ROOMS:")
print(len(room_df))

print("\nROOM OCCUPANCY DETAILS:")
print(room_df[["room_number", "capacity", "occupied_count"]])

print("\nAVAILABLE ROOMS:")
print(room_df["capacity"].sum() - room_df["occupied_count"].sum())

import matplotlib.pyplot as plt

plt.bar(room_df["room_number"], room_df["occupied_count"])
plt.xlabel("Room Number")
plt.ylabel("Occupied Students")
plt.title("Room Occupancy Analysis")
plt.show()

fee_query = "SELECT * FROM Fee"

fee_df = pd.read_sql(fee_query, con)

print("\nFEE DETAILS")
print(fee_df)

print("\nTOTAL FEE AMOUNT:")
print(fee_df["amount"].sum())

print("\nPAYMENT STATUS COUNT:")
print(fee_df["payment_status"].value_counts())
status_count = fee_df["payment_status"].value_counts()

status_count.plot(kind="bar")
plt.xlabel("Payment Status")
plt.ylabel("Number of Students")
plt.title("Fee Payment Analysis")
plt.show()

allocation_query = """
SELECT s.student_name, r.room_number,
       a.allocation_date, a.status
FROM Room_Allocation a
JOIN Student s ON a.student_id = s.student_id
JOIN Room r ON a.room_id = r.room_id
"""

allocation_df = pd.read_sql(allocation_query, con)

print("\nROOM ALLOCATION DETAILS")
print(allocation_df)

print("\nALLOCATION STATUS COUNT:")
print(allocation_df["status"].value_counts())
allocation_count = allocation_df["status"].value_counts()

allocation_count.plot(kind="bar")
plt.xlabel("Allocation Status")
plt.ylabel("Number of Students")
plt.title("Room Allocation Analysis")
plt.show()
con.close()