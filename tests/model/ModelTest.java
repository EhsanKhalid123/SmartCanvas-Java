package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class ModelTest {

    private Model model;
    private User user;

    @Before
    public void setUp() {
        model = new Model();
        user = new User ("username", "firstname", "lastname", "password", null);
    }

    @Test
    public void testHashPasswords() throws NoSuchAlgorithmException {
        String hashPassword = Model.hashPassword("test");
        Assert.assertEquals("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5" +
                "f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff", hashPassword);
    }

    @Test
    public void testGetCurrentUser() {
        model.setCurrentUser(user);
        String username = model.getCurrentUser().getUsername();
        Assert.assertEquals("username", username);
    }

}