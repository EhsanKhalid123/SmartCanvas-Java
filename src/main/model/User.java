package model;

import javafx.scene.image.Image;

public class User {

    // Variable Declaration
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Image dp;

    // Default Constructor for User
    public User() {
    }

    // User Object Constructor to create a user
    public User(String username, String password, String firstname, String lastname, Image dp) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dp = dp;
    }

    // Getter for Username
    public String getUsername() {
        return username;
    }

    // Setter for Username
    public void setUsername(String username) {
        this.username = username;
    }

    // Setter for Password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for Firstname
    public String getFirstname() {
        return firstname;
    }

    // Setter for Firstname
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    // Getter for Lastname
    public String getLastname() {
        return lastname;
    }

    // Setter for Lastname
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    // Getter for Image
    public Image getDp() {
        return dp;
    }

    // Setter for Image
    public void setDp(Image dp) {
        this.dp = dp;
    }

    // To string method to print the user details without memory addresses and properly
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dp=" + dp +
                '}';
    }
}
