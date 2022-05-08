package smartcanvas;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginClose;
    @FXML
    private Hyperlink loginNewUser;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Button loginSignin;
    @FXML
    private TextField loginUsername;
    @FXML
    private Label message;

    @FXML
    public void login() throws IOException {

        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (username.isBlank()) {
            message.setText("Please Enter Username");
        } else if (password.isBlank()) {
            message.setText("Please Enter Password");
        }
        else {
            for (int i = 0; i < UserDao.users.size(); i++) {
                if (UserDao.users.get(i).getUsername().contains(username) && UserDao.users.get(i).getPassword().contains(password)) {
                    UserDao.loggedUser.add(username);
                    Main.setWindow("SmartCanvas", 900, 610, "SmartCanvas", true);
                }
            }
            message.setText("Username and Password doesn't exist!");
        }
    }

    @FXML
    void registerWindow() throws Exception {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        stage.setTitle("Create a new user");
        stage.setScene(new Scene(root, 392, 590));
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    public void close() {
        Platform.exit();
    }


}
