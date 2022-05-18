package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import model.User;

import javax.imageio.ImageIO;
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

    private Pane canvas = new Pane();

    private Model model;
    private Image image;
    private Node selectedElement;
    private Color colorOfSelectedElement;
    private Text text;
    private Circle circle;
    private Rectangle rectangle;
    private ImageView imageView;

    // Original coordinates of the node before each dragging
    private double originX;
    private double originY;

    @FXML
    public void initialize() throws SQLException {
        getDetails();
        setPaneZoom(100);
        zoomSlider.setValue(100);
        nodeInfo.setText(String.format("x: %.0f y: %.0f w: %.0f h: %.0f angle: %.0f°", 0.00 + 0.00, 0.00 + 0.00, 0.00, 0.00, 0.00));
        fileMenu.setOnShowing(event -> {
            if (canvas.getChildren().isEmpty() == true) {
                clearCanvasMenu.setDisable(true);
            }
            if (borderPane.getChildren().contains(canvas) == false) {
                saveAsMenu.setDisable(true);
            }
        });

        editMenu.setOnShowing(event -> {
            if (selectedElement == null) {
                deleteMenu.setDisable(true);
            }
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
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit profile");
        stage.setScene(new Scene(root, 430, 268));
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.showAndWait();

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
        ncc.SMC(borderPane, canvas, saveAsMenu, fileMenu);
        stage.setTitle("Create a new canvas");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, 300, 145));
        stage.setResizable(false);
        Image icon = new Image(Model.class.getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
        stage.showAndWait();
    }

    @FXML
    void zoom() {
        zoomSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            setPaneZoom(newValue.intValue());
        });
    }

    private void setPaneZoom(int percentage) {
        if (canvas != null) {
            canvas.setScaleX(percentage / 100.0);
            canvas.setScaleY(percentage / 100.0);
            zoomPercentage.setText(percentage + "%");
        }
    }

    @FXML
    void addImage() {
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            Stage stage = new Stage();
            imageView = new ImageView();
            if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
                canvas.getChildren().add(imageView);
            }

            FileChooser fileChooser = new FileChooser();
            // Set extension filter
            ExtensionFilter bitmapFilter = new FileChooser.ExtensionFilter("Bitmap Files (*.bmp;*.dib)", "*.bmp", "*.dib");
            ExtensionFilter jpegFilter = new FileChooser.ExtensionFilter("JPEG (*.jpg;*.jpeg;*.jpe;*.jiff)",
                    "*.jpg", "*.jpeg", "*.jpe", "*.jiff");
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
            selectEvent(imageView);
        }
    }

    @FXML
    void addRectangle() {
        rectangle = new Rectangle(100, 100, 100, 100);
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(rectangle);
        }
        rectangle.setFill(Color.valueOf("#1bbcd1"));
        clearCanvasMenu.setDisable(false);
        calcCoordinates(rectangle);
        selectEvent(rectangle);
    }

    @FXML
    void addCircle() {
        circle = new Circle(70, 70, 50);
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(circle);
        }
        circle.setFill(Color.valueOf("#0ec943"));
        clearCanvasMenu.setDisable(false);
        calcCoordinates(circle);
        selectEvent(circle);
    }

    @FXML
    void addText() {
        text = new Text(20, 20, "Text");

        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(text);
        }
        text.setFont(Font.font(12));
        clearCanvasMenu.setDisable(false);
        calcCoordinates(text);
        selectEvent(text);

    }

    @FXML
    void clearCanvas() {
        if (canvas.getChildren().isEmpty() == false) {
            canvas.getChildren().clear();
            clearCanvasMenu.setDisable(true);
            nodeInfo.setText(String.format("x: %.0f y: %.0f w: %.0f h: %.0f angle: %.0f°", 0.00 + 0.00, 0.00 + 0.00, 0.00, 0.00, 0.00));
            deleteMenu.setDisable(true);
        }
    }


    @FXML
    void saveAs() {
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            WritableImage wim = new WritableImage((int) canvas.getHeight(), (int) canvas.getWidth());
            canvas.snapshot(null, wim);
            FileChooser fileChooser = new FileChooser();
            Stage stage = new Stage();

            File file = fileChooser.showSaveDialog(stage);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
            } catch (Exception e) {
            }

        }

    }

    @FXML
    void colourCanvas() {

    }


    @FXML
    void deleteButton() {
        canvas.getChildren().remove(selectedElement);
        deleteMenu.setDisable(true);
        if (canvas.getChildren().isEmpty() == true) {
            clearCanvasMenu.setDisable(true);
        }
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
    }

    public void selectEvent(Node node) {
        node.setOnMouseClicked(e -> {
            unselect();
            select(node);
            node.requestFocus();
        });
    }

    public void select(Node node) {
        selectedElement = node;
        deleteMenu.setDisable(false);
//        colorOfSelectedElement = (Color) selectedElement.getStroke();
        if (selectedElement instanceof Circle) {
            circle.setStroke(Color.RED);
        } else if (selectedElement instanceof Rectangle) {
            rectangle.setStroke(Color.RED);
        } else if (selectedElement instanceof Text) {
            text.setStroke(Color.RED);
        } else if (selectedElement instanceof ImageView) {
            imageView.setStyle("-fx-opacity: 50%");
        }

    }

    public void unselect() {
        if (selectedElement instanceof Circle) {
            circle.setStroke(colorOfSelectedElement);
        } else if (selectedElement instanceof Rectangle) {
            rectangle.setStroke(colorOfSelectedElement);
        } else if (selectedElement instanceof Text) {
            text.setStroke(colorOfSelectedElement);
        } else if (selectedElement instanceof ImageView) {
            imageView.setStyle("-fx-opacity: 100%");
        }

        selectedElement = null;
    }

    private void move(Node node, double dx, double dy) {
        double x = node.getBoundsInParent().getMinX();
        double y = node.getBoundsInParent().getMinY();
        double h = node.getBoundsInParent().getHeight();
        double w = node.getBoundsInParent().getWidth();
        nodeInfo.setText(String.format("x: %.0f y: %.0f w: %.0f h: %.0f angle: %.0f°", x + dx, y + dy, w, h, 0.00));
        node.relocate(x + dx, y + dy);
    }

}
