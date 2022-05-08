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
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    public Label confirmMessage;
    @FXML
    private Button createUser;
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

    private Image image;

    private final Color error = Color.web("#ff1c1c");

    @FXML
    public void registerUser() {
        confirmMessage.setTextFill(error);
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        String firstname = registerFirstname.getText();
        String lastname = registerLastname.getText();

        if (username == null || username.isEmpty()) {
            confirmMessage.setText("Please enter a username!");
            return;
        } else if (firstname == null || firstname.isEmpty()) {
            confirmMessage.setText("Please enter a firstname!");
            return;
        } else if (lastname == null || lastname.isEmpty()) {
            confirmMessage.setText("Please enter a lastname!");
            return;
        } else if (password == null || password.isEmpty()) {
            confirmMessage.setText("Please enter a password!");
            if (password != null || !password.isEmpty()){
                //Todo
                // PASSWORD WILL BE HASHED HERE
            }
        } else if (registerDP.getImage() == null){
            image = new Image(getClass().getResourceAsStream("/views/defaultDP.png"));
            registerDP.setImage(image);
            picRectangle.setStrokeWidth(0);
        } else if (registerDP.getImage() != null){
            image = registerDP.getImage();
            registerDP.setImage(image);
            picRectangle.setStrokeWidth(0);
        }

//        Model.users.add(new User(username, password, firstname, lastname, image));
//        confirmMessage.setTextFill(Color.GREEN);
//        confirmMessage.setText("Created user " + username);
        try {
            Model model = new Model();
            model.getUserDao().createUser(username, password, firstname, lastname, image);
//            model.getUserDao().createUser(username, password, firstname, lastname);
            confirmMessage.setTextFill(Color.GREEN);
            confirmMessage.setText("Created user " + username);
        } catch (SQLException | FileNotFoundException e) {
            confirmMessage.setTextFill(Color.RED);
            System.out.println(e.getMessage());
            confirmMessage.setText("Error " + e.getMessage());
        }


    }

    @FXML
    void profilePicture() {
        FileChooser fileChooser = new FileChooser();
//        File file;

        Stage stage = new Stage();
        try {
            Model.file = fileChooser.showOpenDialog(stage);
            FileInputStream fileInputStream = new FileInputStream(Model.file);
            Image image;
            if (Model.file == null){
                Model.file = new File("/views/defaultDP.png");
                FileInputStream fileInputStream1 = new FileInputStream(Model.file);
                image = new Image(fileInputStream1);
            } else {
//                image = new Image(Model.file.toURI().toString());
                image = new Image(fileInputStream);
            }
            picRectangle.setStrokeWidth(0);
            registerDP.setImage(image);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }


    public void close() {
        Stage stage = (Stage) registerClose.getScene().getWindow();
        stage.close();
    }

}
