// DatabaseConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DATABASE_URL = "jdbc:sqlite:Webshop.db";

    // Private constructor to prevent direct instantiation
    private DatabaseConnection() {
        try {
            // Register JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish the connection
            this.connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection error");
            e.printStackTrace();
        }
    }

    // Singleton method to get the instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Improved method to get the connection - checks if connection is valid and reconnects if needed
    public Connection getConnection() {
        try {
            // Check if connection is closed or invalid
            if (connection == null || connection.isClosed()) {
                // Reestablish the connection
                this.connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Database connection reestablished");
            }
        } catch (SQLException e) {
            System.out.println("Error checking/reestablishing database connection");
            e.printStackTrace();

            // Attempt to create a new connection as a fallback
            try {
                this.connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Database connection reestablished after error");
            } catch (SQLException ex) {
                System.out.println("Failed to reestablish database connection");
                ex.printStackTrace();
            }
        }
        return connection;
    }

    // Close the connection when application shuts down
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.out.println("Error closing database connection");
                e.printStackTrace();
            }
        }
    }
}