package dao;

import javafx.scene.image.Image;
import model.Model;
import model.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDaoImpl implements UserDao {
    private final String TABLE_NAME = "users";

    public UserDaoImpl() {
    }

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF Not EXISTS " + TABLE_NAME + " (username VARCHAR(10) Not NULL,"
                    + "hashedPassword VARCHAR(255) Not NULL," + "firstname VARCHAR(10) Not NULL," + "lastname VARCHAR(10) Not NULL," + "image LONGBLOB," + "PRIMARY KEY (username))";
            stmt.executeUpdate(sql);
        }
    }

    @Override
    public User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    return user;
                }
                return null;
            }
        }
    }

    @Override
    public User getUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND hashedPassword = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
                return null;
            }
        }
    }

    @Override
    public User getUser(String username, String password, String firstname, String lastname, Image dp) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ? AND firstname = ? AND lastname = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstname);
            stmt.setString(4, lastname);
//            stmt.setBlob(5, (Blob) dp);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
//                    Blob blob = rs.getBlob(String.valueOf(dp));
//                    InputStream inputStream = blob.getBinaryStream();
//                    Image image = new Image(inputStream);
//                    user.setDp(image);
                    return user;
                }
                return null;
            }
        }
    }

    @Override
    public User createUser(String username, String password, String firstname, String lastname, Image dp) {
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            String hashedPassword = Model.hashPassword(password);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, firstname);
            stmt.setString(4, lastname);
            FileInputStream fileInputStream = new FileInputStream(Model.file);
            stmt.setBinaryStream(5, fileInputStream, (int) Model.file.length());

            stmt.executeUpdate();
            return new User(username, password, firstname, lastname, dp);
        } catch (SQLException | FileNotFoundException | NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return new User(username, password, firstname, lastname, dp);
        }
    }
}
