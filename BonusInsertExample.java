import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class BonusInsertExample {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/aits";  // Your DB URL
        String username = "root";                            // Your DB username
        String password = "Navitha@7955";                            // Your DB password

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Load MySQL driver (optional for recent JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to DB
            connection = DriverManager.getConnection(jdbcURL, username, password);

            // Prepare SQL INSERT statement
            String sql = "INSERT INTO BONUS (ENAME, JOB, SAL, COMM) VALUES (?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);

            // Set values (example data)
            preparedStatement.setString(1, "john");   // ENAME
            preparedStatement.setString(2, "manager");   // JOB
            preparedStatement.setInt(3, 4000);          // SAL
            preparedStatement.setInt(4, 600);           // COMM

            // Execute the insert
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new bonus record was inserted successfully!");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL error:");
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
