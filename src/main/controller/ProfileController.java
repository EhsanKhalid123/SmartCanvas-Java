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

    private Image initialImage;
    private Image image;
    private Text userName;
    private ImageView dpImage;
    private Model model;
    private User user;

    @FXML
    public void initialize() throws SQLException {
        getLoginDetail();
    }

    public void SMC(Text userName, ImageView dpImage){
        this.userName = userName;
        this.dpImage = dpImage;
    }

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

    @FXML
    void cancel() {
        Stage stage = (Stage) profileClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    void ok() throws SQLException {

        if (profileFirstname.getText().isBlank()) {
            message.setText("Firstname is empty!");
            return;
        }
        if (profileLastName.getText().isBlank()) {
            message.setText("Lastname is empty!");
            return;
        }

        if (!profileFirstname.getText().equals(user.getFirstname())) {
            User user = model.getUserDao().getUser(Model.loggedUser);
            userName.setText(profileFirstname.getText() + " " + user.getLastname());
            model.getUserDao().updateUser(profileFirstname.getText(), user.getLastname(), user.getUsername());
        }
        if (!profileLastName.getText().equals(user.getLastname())) {
            User user = model.getUserDao().getUser(Model.loggedUser);
            userName.setText(user.getFirstname() + " " + profileLastName.getText());
            model.getUserDao().updateUser(user.getFirstname(), profileLastName.getText(), user.getUsername());
        }
        if (!profilePic.getImage().equals(user.getDp())) {
            dpImage.setImage(profilePic.getImage());
            model.getUserDao().updateUser(user.getUsername(), profilePic.getImage());
        }

        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();

    }

    @FXML
    void profilePicture() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();

        model.file = fileChooser.showOpenDialog(stage);
        if (model.file == null) {
            profilePic.setImage(initialImage);
        } else {
            image = new Image(model.file.toURI().toString());
            profilePic.setImage(image);
        }

    }

}
