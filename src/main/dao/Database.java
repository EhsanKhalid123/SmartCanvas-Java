package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // Variable Declaration
    // URL pattern for database
    private static final String createDB_URL = "jdbc:mysql://localhost/";
    private static final String DB_URL = "jdbc:mysql://localhost/application";

    // Method to create Database
    public void createDatabase() {
        // Creates a database and connects to the local host
        try (Connection conn = DriverManager.getConnection(createDB_URL, "root", "password");
             Statement stmt = conn.createStatement()
        ) {
            // Creates DB with name application
            String DB_NAME = "application";
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            // Exception Handling
            System.out.println("An error occurred - Possible Cause: connection to database was refused");
        }
    }

    // Connects to existing DB
    public static Connection getConnection() throws SQLException {
        // DriverManager is the basic service for managing a set of JDBC drivers
        // Can also pass username and password
        return DriverManager.getConnection(DB_URL, "root", "password");
    }
}
