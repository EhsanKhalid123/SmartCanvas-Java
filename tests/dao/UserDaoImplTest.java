package dao;

import model.Model;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class UserDaoImplTest {

    private Model model;
    private User user;

    @Before
    public void setUp() {
        model = new Model();
    }

    @Test
    public void getUserFromDB() throws SQLException {
        user = model.getUserDao().getUser("z");
        String expectedUser = "User{username='z', password='z', firstname='z', lastname='z', dp=null}";
        Assert.assertEquals(expectedUser, user.toString());
    }
}