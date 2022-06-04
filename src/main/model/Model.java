package model;

import dao.UserDao;
import dao.UserDaoImpl;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Model {

    // Variable Declarations
    public static File file;
    public static String loggedUser = "";
    private final UserDao userDao;
    private User currentUser;

    // Method to Hash Passwords and convert it into hex format
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing MD5
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

        // digest() method is called to calculate message digest
        //  of an input digest() return array of byte
        byte[] hashedPassword = messageDigest.digest(password.getBytes());

        // Convert byte array into signum representation
        BigInteger bigInteger = new BigInteger(1, hashedPassword);

        // Convert message digest into hex value
        String hashedText = bigInteger.toString(16);
        while (hashedText.length() < 32) {
            hashedText = "0" + hashedText;
        }

        // Returns hashed password
        return hashedText;
    }

    // Creates an UserDao implementation object
    public Model() {
        userDao = new UserDaoImpl();
    }

    // Sets up the DB, creates a table
    public void setup() throws SQLException {
        userDao.setup();
    }

    // Gets users from the Database
    public UserDao getUserDao() {
        return userDao;
    }

    // Gets the current User that is logged in which is assigned when user logs in
    public User getCurrentUser() {
        return this.currentUser;
    }

    // Sets the current user at login
    public void setCurrentUser(User user) {
        currentUser = user;
    }

}

