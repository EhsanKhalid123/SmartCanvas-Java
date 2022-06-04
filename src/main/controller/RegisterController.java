package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

public class RegisterController {
    // Variable Declarations - Javafx Components Based on SceneBuilder
    @FXML
    public Label confirmMessage;
    @FXML
    private Button registerClose;
    @FXML
    private ImageView registerDP;
    @FXML
    private TextField registerFirstname;
    @FXML
    private TextField registerLastname;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private TextField registerUsername;
    @FXML
    private Rectangle picRectangle;

    // Variable Declarations
    private Image image;
    private final Color error = Color.web("#ff1c1c");

    // ActionEvent for Register Button Declared in Scene Builder
    @FXML
    public void registerUser() throws IOException, URISyntaxException {
        // Sets label colour to red
        confirmMessage.setTextFill(error);
        // Gets user input details
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        String firstname = registerFirstname.getText();
        String lastname = registerLastname.getText();

        // Conditional Statement for Validating User Input
        if (username == null || username.isEmpty()) {
            confirmMessage.setText("Please enter a username!");
            return;
            // Validating User Input
        } else if (firstname == null || firstname.isEmpty()) {
            confirmMessage.setText("Please enter a firstname!");
            return;
            // Validating User Input
        } else if (lastname == null || lastname.isEmpty()) {
            confirmMessage.setText("Please enter a lastname!");
            return;
            // Validating User Input
        } else if (password == null || password.isEmpty()) {
            confirmMessage.setText("Please enter a password!");
            // Validating User Input
        } else if (registerDP.getImage() == null) {
            // If image is not chosen or is null then default image is chosen
            URL defaultImage = getClass().getResource("/views/defaultDP.png");
            assert defaultImage != null;
            Model.file = new File(defaultImage.toURI());
            FileInputStream fileInputStream = new FileInputStream(Model.file);
            image = new Image(fileInputStream);
            registerDP.setImage(image);
            picRectangle.setStrokeWidth(0);
            // If image is selected then display that image
        } else if (registerDP.getImage() != null) {
            image = registerDP.getImage();
            registerDP.setImage(image);
            picRectangle.setStrokeWidth(0);
        }

        try {
            Model model = new Model();
            // Checks the DB for username (Primary Key) If already exists then displays error
            if (model.getUserDao().getUser(username) != null) {
                confirmMessage.setTextFill(Color.RED);
                confirmMessage.setText("Username already Exists");
                return;
            }
            // If user does not exist then created a user in DB with selected user Inputs
            model.getUserDao().createUser(username, password, firstname, lastname, image);
            confirmMessage.setTextFill(Color.GREEN);
            confirmMessage.setText("Created user " + username);
        } catch (SQLException e) {
            // Exception Handling
            confirmMessage.setTextFill(Color.RED);
            confirmMessage.setText("Error creating User");
            System.out.println(e.getMessage());
        }
    }

    // ActionEvent for profile Picture Button Declared in Scene Builder
    @FXML
    void profilePicture() {
        // Object Creation
        FileChooser fileChooser = new FileChooser();
        Image image;
        Stage stage = new Stage();
        Model.file = fileChooser.showOpenDialog(stage);

        try {
            // If no file is chosen or is null then select a default Image
            if (Model.file == null) {
                URL defaultImage = getClass().getResource("/views/defaultDP.png");
                assert defaultImage != null;
                Model.file = new File(defaultImage.toURI());
                image = new Image(String.valueOf(defaultImage));
            } else {
                // If file is chosen then convert that into FIS and store it into image variable
                FileInputStream fileInputStream = new FileInputStream(Model.file);
                image = new Image(fileInputStream);
            }
            // Sets the ImageView with the image
            picRectangle.setStrokeWidth(0);
            registerDP.setImage(image);
        } catch (Exception e) {
            // Exception Handling
            System.out.println(e.getMessage());
        }
    }

    // ActionEvent for Close Button Declared in Scene Builder
    public void close() {
        // Closes the window
        Stage stage = (Stage) registerClose.getScene().getWindow();
        stage.close();
    }

}
