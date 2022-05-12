package model;

import dao.UserDao;
import dao.UserDaoImpl;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Model {

    public static File file;
    public static InputStream inputStream;
    public static List<User> users = new ArrayList<>();
    public static List<String> loggedUser = new ArrayList<>();
    public static Canvas canvas = new Canvas();
    public static Pane canvasPane = new Pane();
    public static Pane pane = new Pane();

    public static List<User> getUsers(){
        return users;
    }

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


    private UserDao userDao;
    private User currentUser;

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

