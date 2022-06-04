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
import java.util.Objects;

public class LoginController {

    // Variable Declarations - Javafx Components Based on SceneBuilder
    @FXML
    private PasswordField loginPassword;
    @FXML
    private TextField loginUsername;
    @FXML
    private Label message;

    // Login Button Action EventHandler Created in SceneBuilder
    @FXML
    public void login() throws IOException, SQLException, NoSuchAlgorithmException {

        // Variable Declaration - Gets textFields input
        String username = loginUsername.getText();
        String password = loginPassword.getText();
        // Hashes Password
        String hashedPassword = Model.hashPassword(password);

        // Conditional Statement to Validate User Input Field
        if (username.isBlank()) {
            message.setText("Please Enter Username");
        } else if (password.isBlank()) {
            message.setText("Please Enter Password");
        } else {
            Model model = new Model();
            User user;
            // Compare user input data to data stored in DB
            user = model.getUserDao().getUser(username, hashedPassword);
            // If data matches and is not null then login the user
            if (user != null) {
                model.setCurrentUser(user);
                Model.loggedUser = user.getUsername();
                Main.setWindow("resources/views/SmartCanvas", 900, 610, "SmartCanvas", true);
            } else {
                // Error Message
                message.setText("Username and Password doesn't exist!");
            }
        }
    }

    // Register Button Action EventHandler Created in SceneBuilder
    @FXML
    void registerWindow() throws Exception {
        // Steps to create a new Window and Basic Properties
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Register.fxml")));
        stage.setTitle("Create a new user");
        stage.setScene(new Scene(root, 392, 590));
        stage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/views/Whiteboard-512.png")));
        stage.getIcons().add(icon);
        stage.show();
    }

    // Close Button Action EventHandler Created in SceneBuilder
    @FXML
    public void close() {
        Platform.exit();
    }

}
