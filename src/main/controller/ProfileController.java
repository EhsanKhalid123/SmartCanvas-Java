package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.sql.SQLException;

public class ProfileController {

    // Variable Declarations - Javafx Components Based on SceneBuilder
    @FXML
    private TextField profileFirstname;
    @FXML
    private Button profileClose;
    @FXML
    private TextField profileLastName;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label profileUsername;
    @FXML
    private Button ok;
    @FXML
    private Label message;

    // Variable Declarations
    private Image initialImage;
    private Text userName;
    private ImageView dpImage;
    private Model model;
    private User user;

    // Gets Login Details and Displays them as this controller is opened
    @FXML
    public void initialize() throws SQLException {
        getLoginDetail();
    }

    // Elements are passed from SmartCanvasController to this controller
    public void SMC(Text userName, ImageView dpImage) {
        this.userName = userName;
        this.dpImage = dpImage;
    }

    // Gets Logged in Users Details from DB and sets the information in the elements
    @FXML
    public void getLoginDetail() throws SQLException {
        model = new Model();
        user = model.getUserDao().getUser(Model.loggedUser);
        profileUsername.setText(user.getUsername());
        profileFirstname.setText(user.getFirstname());
        profileLastName.setText(user.getLastname());
        initialImage = user.getDp();
        profilePic.setImage(initialImage);
    }

    // ActionEvent for Cancel Button Declared in Scene Builder
    @FXML
    void cancel() {
        // Closes the window
        Stage stage = (Stage) profileClose.getScene().getWindow();
        stage.close();
    }

    // ActionEvent for ok Button Declared in Scene Builder
    @FXML
    void ok() throws SQLException {
        // Validation Checks for user input
        if (profileFirstname.getText().isBlank()) {
            message.setText("Firstname is empty!");
            return;
        }
        // Validation Checks for user input
        if (profileLastName.getText().isBlank()) {
            message.setText("Lastname is empty!");
            return;
        }
        // Checks if user detail has been changed
        if (!profileFirstname.getText().equals(user.getFirstname())) {
            // If user changed their details then update on the view and in the DB
            User user = model.getUserDao().getUser(Model.loggedUser);
            userName.setText(profileFirstname.getText() + " " + user.getLastname());
            model.getUserDao().updateUser(profileFirstname.getText(), user.getLastname(), user.getUsername());
        }
        // Checks if user detail has been changed
        if (!profileLastName.getText().equals(user.getLastname())) {
            // If user changed their details then update on the view and in the DB
            User user = model.getUserDao().getUser(Model.loggedUser);
            userName.setText(user.getFirstname() + " " + profileLastName.getText());
            model.getUserDao().updateUser(user.getFirstname(), profileLastName.getText(), user.getUsername());
        }
        // Checks if user detail has been changed
        if (!profilePic.getImage().equals(user.getDp())) {
            // If user changed their details then update on the view and in the DB
            dpImage.setImage(profilePic.getImage());
            model.getUserDao().updateUser(user.getUsername(), profilePic.getImage());
        }

        // Closes the window
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    // ActionEvent for Selecting Profile Picture Button Declared in Scene Builder
    @FXML
    void profilePicture() {
        // Object Creation
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();

        // Assigns the selected file to model.file
        Model.file = fileChooser.showOpenDialog(stage);
        // If file is null then chooses a default photo and displays it
        if (Model.file == null) {
            profilePic.setImage(initialImage);
        } else {
            // If file not null then displays the chosen photo
            Image image = new Image(Model.file.toURI().toString());
            profilePic.setImage(image);
        }
    }
}
