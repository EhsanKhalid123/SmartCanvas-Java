package model;

import dao.UserDao;
import dao.UserDaoImpl;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Model {

    public static File file;
    public static String loggedUser = new String();
    private UserDao userDao;
    private User currentUser;

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing MD5
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        // digest() method is called to calculate message digest
        //  of an input digest() return array of byte
        byte[] hashedPassword = md.digest(password.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, hashedPassword);

        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public Model() {
        userDao = new UserDaoImpl();
    }

    public void setup() throws SQLException {
        userDao.setup();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

}

