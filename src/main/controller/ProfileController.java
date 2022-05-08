package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;

import java.io.File;
import java.io.IOException;

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

    private Image initialImage;
    private Image image;
    private Image image2;
    private String firstName;
    private String lastName;

    @FXML
    public void initialize() {
        getLoginDetail();
    }

    @FXML
    public void getLoginDetail() {
        for (int i = 0; i < Model.users.size(); i++) {
            if (Model.users.get(i).getUsername().contains(Model.loggedUser.get(0))) {
                profileUsername.setText(Model.users.get(i).getUsername());
                profileFirstname.setText(Model.users.get(i).getFirstname());
                profileLastName.setText(Model.users.get(i).getLastname());
                initialImage = Model.users.get(i).getDp();
                profilePic.setImage(initialImage);
                firstName = Model.users.get(i).getFirstname();
                lastName = Model.users.get(i).getLastname();
            }
        }
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) profileClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    void ok() throws IOException {

        for (int i = 0; i < Model.users.size(); i++) {
            if (!profileFirstname.getText().equals(firstName)) {
                Model.users.get(i).setFirstname(profileFirstname.getText());
            }
            if (!profileLastName.getText().equals(lastName)) {
                Model.users.get(i).setLastname(profileLastName.getText());
            }
            if (!profilePic.getImage().equals(initialImage)){
                Model.users.get(i).setDp(image);
            }
        }

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("SmartCanvas.fxml"));
//        controller.Main.openFXML("SmartCanvas");
//        loader.load();
//        SmartCanvasController smc = loader.getController();
//        smc.getDetails();

        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();


    }


    @FXML
    void profilePicture() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file;
        try {
            file = fileChooser.showOpenDialog(stage);
            if (file == null) {
                profilePic.setImage(initialImage);
            } else {
                image = new Image(file.toURI().toString());
                profilePic.setImage(image);
                for (int i = 0; i < Model.users.size(); i++) {
                    if (Model.users.get(i).getUsername().contains((CharSequence) profileUsername)) {
                        if (!profilePic.getImage().equals(initialImage)) {
                            Model.users.get(i).setDp(image);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

}
