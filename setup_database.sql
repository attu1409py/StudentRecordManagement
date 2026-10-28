
/*
Project: Student Record Management System
Experiment 9 - Lab 1
Database: CollegeDB
Table: dbo.Student

This script creates the database and table if needed,
then restores the expected seven student records.
*/

-- Step 1: Create database if it does not exist
USE master;
GO

IF DB_ID(N'CollegeDB') IS NULL
BEGIN
    CREATE DATABASE CollegeDB;
END;
GO

-- Step 2: Select the project database
USE CollegeDB;
GO

-- Step 3: Create the Student table if it does not exist
IF OBJECT_ID(N'dbo.Student', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Student
    (
        StudentID INT PRIMARY KEY,
        StudentName VARCHAR(100) NOT NULL,
        Department VARCHAR(50) NOT NULL,
        Marks DECIMAL(5,2) NOT NULL
    );
END;
GO

-- Step 4: Reset the student records
DELETE FROM dbo.Student;
GO

-- Step 5: Insert the original final dataset
INSERT INTO dbo.Student
    (StudentID, StudentName, Department, Marks)
VALUES
    (101, 'Aarav Sharma',   'AIDS',     85.50),
    (102, 'Priya Patil',    'Computer', 91.00),
    (103, 'Rohan Desai',    'EXTC',     82.00),
    (104, 'Sneha Joshi',    'ECS',      88.00),
    (106, 'Kavya Mehta',    'AIDS',     92.50),
    (107, 'Rahul Kulkarni', 'Computer', 78.00),
    (108, 'Isha Nair',      'EXTC',     86.50);
GO

-- Step 6: Verify the restored records
SELECT StudentID, StudentName, Department, Marks
FROM dbo.Student
ORDER BY StudentID;
GO
