
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionTest {

    private static final String URL =
        "jdbc:sqlserver://localhost:14331;"
        + "databaseName=CollegeDB;"
        + "integratedSecurity=true;"
        + "encrypt=true;"
        + "trustServerCertificate=true;";

    public static void main(String[] args) {

        String sql =
            "SELECT StudentID, StudentName, Department, Marks "
            + "FROM dbo.Student ORDER BY StudentID";

        try {
            Class.forName(
                "com.microsoft.sqlserver.jdbc.SQLServerDriver"
            );

            try (Connection connection =
                     DriverManager.getConnection(URL)) {

                System.out.println("======================================");
                System.out.println(" JDBC CONNECTION SUCCESSFUL!");
                System.out.println(" Connected to CollegeDB");
                System.out.println("======================================");

                try (PreparedStatement statement =
                         connection.prepareStatement(sql);
                     ResultSet result = statement.executeQuery()) {

                    System.out.printf(
                        "%-12s %-22s %-15s %s%n",
                        "StudentID", "StudentName",
                        "Department", "Marks"
                    );

                    System.out.println(
                        "--------------------------------------------------------------"
                    );

                    int count = 0;

                    while (result.next()) {
                        count++;

                        System.out.printf(
                            "%-12d %-22s %-15s %s%n",
                            result.getInt("StudentID"),
                            result.getString("StudentName"),
                            result.getString("Department"),
                            result.getBigDecimal("Marks").toPlainString()
                        );
                    }

                    System.out.println(
                        "\nTotal student records: " + count
                    );
                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found.");
            System.out.println(e.getMessage());

        } catch (SQLException e) {
            System.out.println("JDBC connection or query failed.");
            System.out.println("Error: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}
