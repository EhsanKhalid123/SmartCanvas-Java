package smartcanvas;

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

import java.io.File;

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
            registerDP.setImage(new Image(getClass().getResourceAsStream("defaultDP.png")));
            picRectangle.setStrokeWidth(0);
        }

        UserDao.users.add(new User(username, password, firstname, lastname, registerDP));

        confirmMessage.setTextFill(Color.GREEN);
        confirmMessage.setText("Created user " + username);

    }

    @FXML
    void profilePicture() {
        FileChooser fileChooser = new FileChooser();
        File file;
        Stage stage = new Stage();
        try {
            file = fileChooser.showOpenDialog(stage);
            Image image;
            if (file == null){
                image = new Image(getClass().getResourceAsStream("defaultDP.png"));
            } else {
                image = new Image(file.toURI().toString());
            }
            picRectangle.setStrokeWidth(0);
            registerDP.setImage(image);

        } catch (Exception e) {

        }



    }


    public void close() {
        Stage stage = (Stage) registerClose.getScene().getWindow();
        stage.close();
    }

}
