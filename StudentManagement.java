
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentManagement {

    private static final String DB_URL =
        "jdbc:sqlserver://localhost:14331;"
        + "databaseName=CollegeDB;"
        + "integratedSecurity=true;"
        + "encrypt=true;"
        + "trustServerCertificate=true;";

    public static void main(String[] args) {

        try {
            Class.forName(
                "com.microsoft.sqlserver.jdbc.SQLServerDriver"
            );
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found.");
            return;
        }

        try (
            Connection con = DriverManager.getConnection(DB_URL);
            Scanner sc = new Scanner(System.in)
        ) {
            System.out.println("\n");
            System.out.println("    STUDENT RECORD MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("Connected to CollegeDB successfully!");

            boolean running = true;

            while (running) {
                System.out.println("\n-*-----❤️    MAIN MENU    ❤️-----*-");
                System.out.println("1. Add New Student");
                System.out.println("2. Display Students");
                System.out.println("3. Update Student Marks");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.println("-------------------------------------");

                int choice = readInt(sc, "Enter your choice 😁: ");

                try {
                    switch (choice) {
                        case 1:
                            addStudent(con, sc);
                            break;

                        case 2:
                            displayStudents(con);
                            break;

                        case 3:
                            updateMarks(con, sc);
                            break;

                        case 4:
                            deleteStudent(con, sc);
                            break;

                        case 5:
                            running = false;
                            System.out.println(
                                "Application closed successfully."
                            );
                            break;

                        default:
                            System.out.println(
                                "Invalid choice. Enter 1 to 5."
                            );
                    }
                } catch (SQLException e) {
                    handleDatabaseError(e);
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to connect to SQL Server.");
            handleDatabaseError(e);
        }
    }

    // CREATE: Insert a new student.
    private static void addStudent(
            Connection con, Scanner sc) throws SQLException {

        System.out.println("\n--- + Add New Student + ---");

        int id = readInt(sc, "Enter Student ID: ");

        if (id <= 0) {
            System.out.println("Student ID must be positive.");
            return;
        }

        String name = readText(sc, "Enter Student Name: ", 100);
        String department = readText(sc, "Enter Department: ", 50);
        BigDecimal marks = readMarks(sc);

        String sql =
            "INSERT INTO dbo.Student " +
            "(StudentID, StudentName, Department, Marks) " +
            "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setBigDecimal(4, marks);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                    "Student added successfully!"
                );
            }

        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                System.out.println(
                    "Student ID already exists. Use a different ID."
                );
            } else {
                throw e;
            }
        }
    }

    // READ: Retrieve all students.
    private static void displayStudents(
            Connection con) throws SQLException {

        System.out.println("\n--- All Student Records ---");

        String sql =
            "SELECT StudentID, StudentName, Department, Marks " +
            "FROM dbo.Student ORDER BY StudentID";

        try (
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            System.out.printf(
                "%-12s %-22s %-15s %s%n",
                "StudentID", "StudentName", "Department", "Marks"
            );

            System.out.println(
                "--------------------------------------------------------------"
            );

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.printf(
                    "%-12d %-22s %-15s %s%n",
                    rs.getInt("StudentID"),
                    rs.getString("StudentName"),
                    rs.getString("Department"),
                    rs.getBigDecimal("Marks").toPlainString()
                );
            }

            if (!found) {
                System.out.println("No student records found.");
            }
        }
    }

    // UPDATE: Change marks using StudentID.
    private static void updateMarks(
            Connection con, Scanner sc) throws SQLException {

        System.out.println("\n--- Update Student Marks ---");

        int id = readInt(sc, "Enter Student ID: ");

        if (id <= 0) {
            System.out.println("Student ID must be positive.");
            return;
        }

        BigDecimal marks = readMarks(sc);

        String sql =
            "UPDATE dbo.Student SET Marks = ? WHERE StudentID = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, marks);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                    "Student marks updated successfully!"
                );
            } else {
                System.out.println(
                    "No student found with ID " + id + "."
                );
            }
        }
    }

    // DELETE: Remove a student using StudentID.
    private static void deleteStudent(
            Connection con, Scanner sc) throws SQLException {

        System.out.println("\n--- Delete Student ---");

        int id = readInt(sc, "Enter Student ID to delete: ");

        if (id <= 0) {
            System.out.println("Student ID must be positive.");
            return;
        }

        String sql =
            "DELETE FROM dbo.Student WHERE StudentID = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                    "Student deleted successfully!"
                );
            } else {
                System.out.println(
                    "No student found with ID " + id + "."
                );
            }
        }
    }

    // Read an integer and retry if the input is invalid.
    private static int readInt(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input. Enter a whole number."
                );
            }
        }
    }

    // Read a required text field within the SQL column length.
    private static String readText(
            Scanner sc, String prompt, int maxLength) {

        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("This field cannot be empty.");
            } else if (input.length() > maxLength) {
                System.out.println(
                    "Maximum length is " + maxLength + " characters."
                );
            } else {
                return input;
            }
        }
    }

    // Read valid marks between 0 and 100, with at most 2 decimals.
    private static BigDecimal readMarks(Scanner sc) {

        while (true) {
            System.out.print("Enter Marks (0-100): ");
            String input = sc.nextLine().trim();

            try {
                BigDecimal marks = new BigDecimal(input);

                if (marks.compareTo(BigDecimal.ZERO) < 0
                        || marks.compareTo(new BigDecimal("100")) > 0) {
                    System.out.println(
                        "Marks must be between 0 and 100."
                    );
                    continue;
                }

                if (marks.stripTrailingZeros().scale() > 2) {
                    System.out.println(
                        "Maximum 2 decimal places are allowed."
                    );
                    continue;
                }

                return marks;

            } catch (NumberFormatException e) {
                System.out.println(
                    "Enter a valid numeric marks value."
                );
            }
        }
    }

    // Recognize SQL Server duplicate-primary-key errors.
    private static boolean isDuplicateKey(SQLException e) {

        SQLException current = e;

        while (current != null) {
            int code = current.getErrorCode();

            if (code == 2627 || code == 2601) {
                return true;
            }

            current = current.getNextException();
        }

        return false;
    }

    // Display meaningful details when a database operation fails.
    private static void handleDatabaseError(SQLException e) {

        System.out.println("Database operation failed.");
        System.out.println("Message: " + e.getMessage());
        System.out.println("SQL State: " + e.getSQLState());
        System.out.println("Error Code: " + e.getErrorCode());
    }
}
