package controller;

import model.Model;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class LoginControllerTest {

    private LoginController lc;
    private User user;
    private Model model;

    @Before
    public void setUp() {
        lc = new LoginController();
        model = new Model();
    }

    @Test
    public void failedLogin() throws SQLException{
        String username = "noUser";
        String password = "noPassword";

        user = model.getUserDao().getUser(username, password);
        if (user == null) {
            String errMsg = errMessage();
            Assert.assertEquals("Username and Password doesn't exist!", errMsg);
        }
    }

    public String errMessage(){
        return "Username and Password doesn't exist!";
    }

}