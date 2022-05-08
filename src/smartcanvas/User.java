package smartcanvas;

import javafx.scene.image.ImageView;

public class User {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private ImageView dp;


    public User(String username, String password, String firstname, String lastname, ImageView dp) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dp = dp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public ImageView getDp() {
        return dp;
    }

    public void setDp(ImageView dp) {
        this.dp = dp;
    }

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
