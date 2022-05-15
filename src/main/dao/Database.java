package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	// URL pattern for database

	private static final String createDB_URL = "jdbc:mysql://localhost/";
	private static final String DB_URL = "jdbc:mysql://localhost/application";
	private final String DB_NAME = "application";

	public void createDatabase(){
		try(Connection conn = DriverManager.getConnection(createDB_URL, "root", "password");
			Statement stmt = conn.createStatement();
		) {

			String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.out.println("An error occurred - Possible Cause: connection to database was refused");
		}
	}

	public static Connection getConnection() throws SQLException {

		// DriverManager is the basic service for managing a set of JDBC drivers
		// Can also pass username and password
		return DriverManager.getConnection(DB_URL, "root", "password");
	}
}
