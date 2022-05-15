package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

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
    private Label nodeInfo;
    @FXML
    private Label zoomPercentage;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Pane borderPane;
    @FXML
    private Menu editMenu;
    @FXML
    private Menu fileMenu;

    private StackPane canvas = new StackPane();

    private Model model;
    private Image image;

    // Original coordinates of the node before each dragging
    private double originX;
    private double originY;

    private Clipboard clipboard;
    ClipboardContent content = new ClipboardContent();

    @FXML
    public void initialize() throws SQLException {
        getDetails();
        fileMenu.setOnShowing(event -> {
            if (canvas.getChildren().isEmpty() == true) {
                clearCanvasMenu.setDisable(true);
            }
        });

        editMenu.setOnShowing(event -> {
            deleteMenu.setDisable(true);
        });
    }

    public void getDetails() throws SQLException {

        model = new Model();
        User user;
        user = model.getUserDao().getUser(Model.loggedUser);
        userName.setText(user.getFirstname() + " " + user.getLastname());
        image = user.getDp();
        dpImage.setImage(image);

    }

    @FXML
    void logout() throws IOException {
        Model.loggedUser = "";
        borderPane.getChildren().remove(canvas);
        Main.setWindow("resources/views/Login", 481, 460, "Welcome to SmartCanvas!", false);
    }

    @FXML
    void viewProfile() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Profile.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController pc = fxmlLoader.getController();
        pc.SMC(userName, dpImage);
        stage.setTitle("Edit profile");
        stage.setScene(new Scene(root, 430, 268));
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.show();

    }

    @FXML
    void aboutMenu() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/views/About.fxml"));
        stage.setTitle("About | Smart Canvas");
        stage.setScene(new Scene(root, 430, 299));
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    @FXML
    void addCanvas() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Model.class.getResource("/views/NewCanvas.fxml"));
        Parent root = fxmlLoader.load();
        NewCanvasController ncc = fxmlLoader.getController();
        ncc.SMC(borderPane, canvas);
        stage.setTitle("Create a new canvas");
        stage.setScene(new Scene(root, 300, 145));
        stage.setResizable(false);
        Image icon = new Image(Model.class.getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    @FXML
    void colourCanvas() {

    }

    @FXML
    void zoom() {
        zoomPercentage.setText("0%");
        zoomSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            zoomPercentage.setText(newValue.intValue() + "%");
            canvas.setScaleX((Double) newValue);
            canvas.setScaleY((Double) newValue);

            canvas.setTranslateX(newValue.doubleValue() * 0);
            canvas.setTranslateY(newValue.doubleValue() * 0);
            borderPane.setTranslateX(newValue.doubleValue() * 0);
            borderPane.setTranslateY(newValue.doubleValue() * 0);
        });

    }

    @FXML
    void addCircle() {

    }

    @FXML
    void addImage() {
        Stage stage = new Stage();
        ImageView imageView = new ImageView();
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(imageView);
        }

        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        ExtensionFilter bitmapFilter = new FileChooser.ExtensionFilter("Bitmap Files (*.bmp;*.dib)", "*.bmp", "*.dib");
        ExtensionFilter jpegFilter = new FileChooser.ExtensionFilter("JPEG (*.jpg;*.jpeg;*.jpe;*.jiff)", "*.jpg", "*.jpeg", "*.jpe", "*.jiff");
        ExtensionFilter gifFilter = new FileChooser.ExtensionFilter("GIF (*.gif)", "*.gif");
        ExtensionFilter tiffFilter = new FileChooser.ExtensionFilter("TIFF (*.tif;*.tiff)", "*.tif", "*.tiff");
        ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
        ExtensionFilter icoFilter = new FileChooser.ExtensionFilter("ICO (*.ico)", "*.ico");
        ExtensionFilter heicFilter = new FileChooser.ExtensionFilter("HEIC (*.heic)", "*.heic");
        ExtensionFilter webpFilter = new FileChooser.ExtensionFilter("WEBP (*.webp)", "*.webp");
        ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All Picture Files", "*.bmp", "*.dib",
                "*.jpg", "*.jpeg", "*.jpe", "*.jiff", "*.gif", "*.tif", "*.tiff", "*.png", "*.ico", "*.heic", "*.webp");
        ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files", "*");

        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(allFilter);
        fileChooser.getExtensionFilters().add(webpFilter);
        fileChooser.getExtensionFilters().add(heicFilter);
        fileChooser.getExtensionFilters().add(icoFilter);
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.getExtensionFilters().add(tiffFilter);
        fileChooser.getExtensionFilters().add(gifFilter);
        fileChooser.getExtensionFilters().add(jpegFilter);
        fileChooser.getExtensionFilters().add(bitmapFilter);
        // Show a file open dialog
        File file = fileChooser.showOpenDialog(stage);

        InputStream fileInputStream;
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        try {
            fileInputStream = new FileInputStream(file);
            imageView.setImage(new Image(fileInputStream));
            clearCanvasMenu.setDisable(false);
        } catch (Exception e) {
            System.out.println("No Image Selected!");
        }

        calcCoordinates(imageView);

    }

    @FXML
    void addRectangle() {

        Rectangle rectangle = new Rectangle(50, 50, 100, 100);

        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(rectangle);
        }

        rectangle.setFill(Color.valueOf("#1bbcd1"));

        calcCoordinates(rectangle);


    }

    @FXML
    void addText() {

    }

    @FXML
    void clearCanvas() {
        if (canvas.getChildren().isEmpty() == false) {
            canvas.getChildren().clear();
        }
    }


    @FXML
    void saveAs() {

    }


    @FXML
    void deleteButton() {

    }

    private void calcCoordinates(Node node) {
        // Move the node by dragging
        node.setOnMousePressed(e -> {
            originX = e.getX();
            originY = e.getY();
        });

        node.setOnMouseDragged(e -> {
            double dx = e.getX() - originX;
            double dy = e.getY() - originY;
            move(node, dx, dy);
        });

        node.setOnMouseClicked(e -> {
            // ToDo // Add select shape Feature and Delete Element if shape selected
            node.requestFocus();
            deleteMenu.setDisable(false);
        });
    }

    private void move(Node node, double dx, double dy) {
        double x = node.getBoundsInParent().getMinX();
        double y = node.getBoundsInParent().getMinY();
        nodeInfo.setText(String.format("x: %.2f y: %.2f", x + dx, y + dy));
//        nodeInfo.setText(String.format("x: %.2f y: %.2f w: %.2f h: %.2f angle: %.2f", x + dx, y + dy, 0.00, 0.00));
        node.relocate(x + dx, y + dy);
        node.setTranslateX(x + dx);
        node.setTranslateY(y + dy);
    }

}
