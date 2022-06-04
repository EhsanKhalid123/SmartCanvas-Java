package dao;

import javafx.scene.image.Image;
import model.User;

import java.sql.SQLException;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface
 * to a database or other persistence mechanism.
 * the DAO maps application calls to the persistence layer and provides some specific data operations
 * without exposing details of the database.
 */
// Interface Class
public interface UserDao {

    // Setup Function
    void setup() throws SQLException;

    // Get User From Database based on Username
    User getUser(String username) throws SQLException;

    // Get User from DB based on Username and Password
    User getUser(String username, String password) throws SQLException;

    // Updates user Details
    void updateUser(String firstname, String lastname, String username);

    // Updates user Details Image
    void updateUser(String username, Image dp);

    // Creates a user in the DB
    User createUser(String username, String password, String firstname, String lastname, Image dp);

}
