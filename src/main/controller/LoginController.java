package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private PasswordField loginPassword;
    @FXML
    private TextField loginUsername;
    @FXML
    private Label message;

    private Model model;

    @FXML
    public void login() throws IOException, SQLException, NoSuchAlgorithmException {

        String username = loginUsername.getText();
        String password = loginPassword.getText();
        String hashedPassword = Model.hashPassword(password);

        if (username.isBlank()) {
            message.setText("Please Enter Username");
        } else if (password.isBlank()) {
            message.setText("Please Enter Password");
        }
        else {
            model = new Model();
            User user;
            user = model.getUserDao().getUser(username, hashedPassword);
            if (user != null){
                model.setCurrentUser(user);
                Model.loggedUser = user.getUsername();
                Main.setWindow("resources/views/SmartCanvas", 900, 610, "SmartCanvas", true);
            } else {
                message.setText("Username and Password doesn't exist!");
            }
        }
    }

    @FXML
    void registerWindow() throws Exception {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/views/Register.fxml"));
        stage.setTitle("Create a new user");
        stage.setScene(new Scene(root, 392, 590));
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    public void close() {
        Platform.exit();
    }


}
