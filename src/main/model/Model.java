package model;

import dao.UserDao;
import dao.UserDaoImpl;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Model {

    public static File file;
    public static List<User> users = new ArrayList<>();
    public static List<String> loggedUser = new ArrayList<>();
    public static Canvas canvas = new Canvas();
    public static Pane canvasPane = new Pane();
    public static Pane pane = new Pane();

    public static List<User> getUsers(){

        return users;
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

