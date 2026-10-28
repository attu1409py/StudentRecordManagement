
# Student Record Management System

A Java console application that manages student records using JDBC and Microsoft SQL Server.

## Features

- Add a new student
- Display all student records
- Update marks using StudentID
- Delete a student using StudentID
- Validate user input
- Handle duplicate StudentIDs

## Technology Stack

- Java JDK 25
- JDBC
- Microsoft SQL Server
- Microsoft JDBC Driver 13.6.0
- Visual Studio Code

## Database

Database: CollegeDB

Table: dbo.Student

Columns:
- StudentID: INT PRIMARY KEY
- StudentName: VARCHAR(100)
- Department: VARCHAR(50)
- Marks: DECIMAL(5,2)

The database setup script is provided in setup_database.sql.

## Dependencies

Download the Microsoft JDBC Driver for SQL Server from:

https://learn.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server

Place these files in the project folders:

- lib/mssql-jdbc-13.6.0.jre11.jar
- auth/mssql-jdbc_auth-13.6.0.x64.dll

These local dependency files are excluded from Git.

## Compile

```powershell
javac -cp ".;lib\mssql-jdbc-13.6.0.jre11.jar" StudentManagement.java
```

## Run

```powershell
java --enable-native-access=ALL-UNNAMED "-Djava.library.path=auth" -cp ".;lib\mssql-jdbc-13.6.0.jre11.jar" StudentManagement
```

The connection settings in the Java source assume a local SQL Server instance listening on TCP port 14331 and using Windows Authentication.
