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
    public void updateUser(String firstname, String lastname, String username) {

//        Note to self
//        Use below syntax when updating direct from parameters passed in
//        String sql = "UPDATE " + TABLE_NAME + " SET firstname = '"+firstname+"', lastname = '"+lastname+"'" + " WHERE username = '"+username+"'";
//        stmt.executeUpdate(sql); <- use this when no stmt.setString stuff is set below the PreparedStatement Line

        String sql = "UPDATE " + TABLE_NAME
                + " SET firstname = ?, lastname = ?"
                + "WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, username);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(String username, Image dp) {

        String sql = "UPDATE " + TABLE_NAME
                + " SET image = ?"
                + "WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (dp != null) {
                URL url = new URL(dp.getUrl());
                File file = new File(url.toURI());
                FileInputStream fileInputStream = new FileInputStream(file);
                stmt.setBinaryStream(1, fileInputStream);
            }
            stmt.setString(2, username);

            stmt.executeUpdate();

        } catch (SQLException | FileNotFoundException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
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
                    user.setPassword(rs.getString("hashedPassword"));
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
