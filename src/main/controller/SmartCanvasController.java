package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import controller.Main;
import model.Model;

import java.io.File;
import java.io.IOException;

public class SmartCanvasController {

    @FXML
    private MenuItem aboutMenu;
    @FXML
    private Button canvasButton;
    @FXML
    private Button circleButton;
    @FXML
    private MenuItem clearCanvasMenu;
    @FXML
    private MenuItem deleteMenu;
    @FXML
    private ImageView dpImage;
    @FXML
    private Button imageButton;
    @FXML
    private Button logoutButton;
    @FXML
    private MenuItem newCanvasMenu;
    @FXML
    private Button profileButton;
    @FXML
    private Button rectButton;
    @FXML
    private MenuItem saveAsMenu;
    @FXML
    private Button textButton;
    @FXML
    private Text userName;
    @FXML
    private Font x3;
    @FXML
    private Font x4;
    @FXML
    private Label zoomPercentage;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Pane borderPane;

    private Image image;
//    private static Image image1 = image;

    @FXML
    public void initialize() {
        getDetails();
        borderPane.getChildren().add(Model.canvasPane);
    }

    public void getDetails(){
        for (int i = 0; i < Model.users.size(); i++) {
            if (Model.users.get(i).getUsername().contains(Model.loggedUser.get(0))) {
                String username = Model.loggedUser.get(0);
                userName.setText(username);
                image = Model.users.get(i).getDp();
                dpImage.setImage(image);
            }
        }
    }

    @FXML
    void logout() throws IOException {
        Model.loggedUser.remove(0);
        Model.pane.getChildren().remove(Model.canvas);
        Model.canvasPane.getChildren().remove(Model.pane);
        Main.setWindow("resources/views/Login", 481, 460, "Welcome to SmartCanvas!", false);
    }

    @FXML
    void viewProfile() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/views/Profile.fxml"));
        stage.setTitle("Edit profile");
        stage.setScene(new Scene(root, 430, 247));
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.show();

    }

    @FXML
    void addCanvas() throws IOException {
        Stage stage = new Stage();
        Parent root2 = FXMLLoader.load(Model.class.getResource("/views/NewCanvas.fxml"));
        stage.setTitle("Create a new canvas");
        stage.setScene(new Scene(root2, 300, 145));
        stage.setResizable(false);
        Image icon = new Image(Model.class.getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.show();

        if (borderPane.getChildren().contains(Model.canvasPane)){
            borderPane.getChildren().remove(Model.canvasPane);
        }
        borderPane.getChildren().add(Model.canvasPane);

    }

    @FXML
    void colourCanvas() {

    }

    @FXML
    void zoom(){
        zoomPercentage.setText("0%");
        zoomSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            zoomPercentage.setText(newValue.intValue() + "%");
            borderPane.setScaleZ(newValue.intValue());
            borderPane.setTranslateZ((Double) newValue);
            borderPane.getTranslateZ();
            Model.canvas.setScaleZ(newValue.intValue());
            System.out.println(newValue.intValue());
            Model.canvas.setTranslateZ(newValue.intValue());

        });

        borderPane.translateZProperty().bind(zoomSlider.valueProperty());

    }

    @FXML
    void addCircle() {

    }

    @FXML
    void addImage() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(10);
        imageView.setFitHeight(10);
        imageView.setPreserveRatio(true);
    }

    @FXML
    void addRectangle() {
        GraphicsContext gc = Model.canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#ff0000"));
        gc.fillRect(100, 100, 200, 200);

    }

    @FXML
    void addText() {

    }

    @FXML
    void clearCanvas() {

    }


    @FXML
    void saveAs() {

    }

}
