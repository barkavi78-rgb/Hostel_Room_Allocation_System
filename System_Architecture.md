
flowchart TD
    A[Admin / User]
    B[Java Application]
    C[Student Management]
    D[Room Management]
    E[Room Allocation]
    F[Fee Management]
    G[(MySQL Database)]
    H[Python Data Analysis]
    I[Reports and Charts]

    A --> B
    B --> C
    B --> D
    B --> E
    B --> F
    C --> G
    D --> G
    E --> G
    F --> G
    G --> H
    H --> I
