package dao;

import javafx.scene.image.Image;
import model.User;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface
 * to a database or other persistence mechanism.
 * the DAO maps application calls to the persistence layer and provides some specific data operations
 * without exposing details of the database.
 */
public interface UserDao {
    void setup() throws SQLException;

    User getUser(String username) throws SQLException;
    User getUser(String username, String password) throws SQLException;
//    User getUser(String username, String password, String firstname, String lastname, Image dp) throws SQLException;

    User createUser(String username, String password, String firstname, String lastname, Image dp) throws SQLException, FileNotFoundException;

}
