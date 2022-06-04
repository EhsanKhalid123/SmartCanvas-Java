package dao;

import javafx.scene.image.Image;
import model.Model;
import model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDaoImpl implements UserDao {
    // Variable Declaration - Table Name in DB
    private final String TABLE_NAME = "users";

    // Default Constructor
    public UserDaoImpl() {
    }

    // Setup DB Table Method
    @Override
    public void setup() throws SQLException {
        // Connects to the DB
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            // Creates sql String for Creating a Table with following columns
            String sql = "CREATE TABLE IF Not EXISTS " + TABLE_NAME + " (username VARCHAR(255) Not NULL,"
                    + "hashedPassword VARCHAR(255) Not NULL," + "firstname VARCHAR(255) Not NULL," +
                    "lastname VARCHAR(255) Not NULL," + "image LONGBLOB," + "PRIMARY KEY (username))";
            stmt.executeUpdate(sql);
        }
    }

    // Updates User Firstname and Lastname based on Username in DB
    @Override
    public void updateUser(String firstname, String lastname, String username) {

        // Update Query for SQL Updates firstname and lastname based on Username
        String sql = "UPDATE " + TABLE_NAME + " SET firstname = ?, lastname = ?" + "WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Replaces the column with parameters
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, username);

            // Updates the DB
            stmt.executeUpdate();

        } catch (SQLException e) {}
    }

    // Update User Image in DB Method
    @Override
    public void updateUser(String username, Image dp) {
        // Update Query For Image based on Username
        String sql = "UPDATE " + TABLE_NAME
                + " SET image = ?"
                + "WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // If image is not null then convert it into FIS and update the image in DB
            if (dp != null) {
                URL url = new URL(dp.getUrl());
                File file = new File(url.toURI());
                FileInputStream fileInputStream = new FileInputStream(file);
                stmt.setBinaryStream(1, fileInputStream);
            }
            stmt.setString(2, username);

            // Update the DB
            stmt.executeUpdate();

        } catch (SQLException | FileNotFoundException | MalformedURLException | URISyntaxException e) {}
    }

    // Get User from DB Method
    @Override
    public User getUser(String username) throws SQLException {
        // Get User Query, Get all Columns for the given username
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            // For this passed username in DB
            stmt.setString(1, username);

            // Returns the following columns for the given username
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("hashedPassword"));
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
                    Blob blob = rs.getBlob("image");
                    InputStream inputStream = blob.getBinaryStream();
                    Image image = new Image(inputStream);
                    user.setDp(image);
                    return user;
                }
                return null;
            }
        }
    }

    // Gets user Details based on Username and Password from the DB
    @Override
    public User getUser(String username, String password) throws SQLException {
        // DB Query String for Getting User
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND hashedPassword = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Compares the columns based on parameters
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Returns the following columns for the username and password given
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("hashedPassword"));
                    return user;
                }
                return null;
            }
        }
    }

    // Create User in Database Method
    @Override
    public User createUser(String username, String password, String firstname, String lastname, Image dp) {
        // SQL Query for Inserting Entries into DB Table
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Insert these parameter values to the columns in this order in the DB
            stmt.setString(1, username);
            String hashedPassword = Model.hashPassword(password);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, firstname);
            stmt.setString(4, lastname);
            FileInputStream fileInputStream = new FileInputStream(Model.file);
            stmt.setBinaryStream(5, fileInputStream, (int) Model.file.length());
            // Executes the Query
            stmt.executeUpdate();
            // Returns a New User
            return new User(username, password, firstname, lastname, dp);
        } catch (SQLException | FileNotFoundException | NoSuchAlgorithmException e) {
            return new User(username, password, firstname, lastname, dp);
        }
    }
}
