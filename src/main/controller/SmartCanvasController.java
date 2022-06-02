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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
import java.io.*;
import java.sql.SQLException;

public class SmartCanvasController {

    @FXML
    private Button Bold;
    @FXML
    private Button Italics;
    @FXML
    private MenuItem aboutMenu;
    @FXML
    private Pane borderPane;
    @FXML
    private Button canvasButton;
    @FXML
    private ColorPicker canvasColourPicker;
    @FXML
    private ToolBar canvasProperties;
    @FXML
    private Button centerAlign;
    @FXML
    private Button changePath;
    @FXML
    private ColorPicker circRectBackground;
    @FXML
    private TextField circRectBorderWidth;
    @FXML
    private ColorPicker circRectColourPicker;
    @FXML
    private ToolBar circRectProperties;
    @FXML
    private Button circleButton;
    @FXML
    private MenuItem clearCanvasMenu;
    @FXML
    private MenuItem deleteMenu;
    @FXML
    private ImageView dpImage;
    @FXML
    private Menu editMenu;
    @FXML
    private Menu fileMenu;
    @FXML
    private TextField fontSize;
    @FXML
    private Button imageButton;
    @FXML
    private ToolBar imageProperties;
    @FXML
    private Button leftAlign;
    @FXML
    private Button logoutButton;
    @FXML
    private MenuItem newCanvasMenu;
    @FXML
    private Label nodeInfo;
    @FXML
    private Button profileButton;
    @FXML
    private Button rectButton;
    @FXML
    private Button rightAlign;
    @FXML
    private MenuItem saveAsMenu;
    @FXML
    private ColorPicker textBackgroundColour;
    @FXML
    private ColorPicker textBorderColour;
    @FXML
    private TextField textBorderWidth;
    @FXML
    private Button textButton;
    @FXML
    private ColorPicker textColour;
    @FXML
    private ComboBox<?> textFont;
    @FXML
    private ToolBar textProperties;
    @FXML
    private TextField textText;
    @FXML
    private Text userName;
    @FXML
    private Label zoomPercentage;
    @FXML
    private Slider zoomSlider;


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
            canvasProperties.setVisible(false);
            circRectProperties.setVisible(false);
            textProperties.setVisible(false);
            imageProperties.setVisible(false);
        }
    }


    @FXML
    void saveAs() {
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            WritableImage wim = new WritableImage((int) canvas.getHeight(), (int) canvas.getWidth());
            canvas.snapshot(null, wim);

            FileChooser fileChooser = new FileChooser();
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
        if (borderPane.getChildren().contains(canvas) == true) {
            if (canvasProperties.isDisabled() && !canvasProperties.isVisible()) {
                canvasProperties.setDisable(false);
                canvasProperties.setVisible(true);
                circRectProperties.setVisible(false);
                textProperties.setVisible(false);
                imageProperties.setVisible(false);
                selectedElement = canvas;
                canvasColourPicker.setOnAction(e -> {
                    if (selectedElement == canvas) {
                        colorOfSelectedElement = canvasColourPicker.getValue();
                        ((Pane) selectedElement).setBackground(new Background(new BackgroundFill(colorOfSelectedElement, null, null)));
                    }
                });
            } else if (!canvasProperties.isDisabled() && canvasProperties.isVisible()) {
                canvasProperties.setDisable(true);
                canvasProperties.setVisible(false);
                circRectProperties.setVisible(false);
                textProperties.setVisible(false);
                imageProperties.setVisible(false);
                selectedElement = null;
            }
        }
    }


    @FXML
    void deleteButton() {
        canvas.getChildren().remove(selectedElement);
        canvasProperties.setVisible(false);
        circRectProperties.setVisible(false);
        textProperties.setVisible(false);
        imageProperties.setVisible(false);
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
        if (selectedElement instanceof Pane) {
            canvasProperties.setVisible(true);
            canvasProperties.setDisable(false);
            circRectProperties.setVisible(false);
            textProperties.setVisible(false);
            imageProperties.setVisible(false);

        } else if (selectedElement instanceof Circle) {
            circRectProperties.setVisible(true);
            circRectProperties.setDisable(false);
            canvasProperties.setVisible(false);
            textProperties.setVisible(false);
            imageProperties.setVisible(false);
            colorOfSelectedElement = (Color) circle.getFill();
            circle.setStroke(Color.RED);

            circRectColourPicker.setOnAction(e -> {
                colorOfSelectedElement = circRectColourPicker.getValue();
                circle.setStroke(colorOfSelectedElement);
            });

            circRectBackground.setOnAction(e -> {
                colorOfSelectedElement = circRectBackground.getValue();
                circle.setFill(colorOfSelectedElement);
            });

            circRectBorderWidth.setOnKeyReleased(e -> {
                try {
                    circle.setStrokeWidth(Double.parseDouble(circRectBorderWidth.getText()));
                } catch (Exception ex) {

                }
            });

        } else if (selectedElement instanceof Rectangle) {
            circRectProperties.setVisible(true);
            circRectProperties.setDisable(false);
            canvasProperties.setVisible(false);
            textProperties.setVisible(false);
            imageProperties.setVisible(false);
            colorOfSelectedElement = (Color) rectangle.getFill();
            rectangle.setStroke(Color.RED);

            circRectColourPicker.setOnAction(e -> {
                colorOfSelectedElement = circRectColourPicker.getValue();
                rectangle.setStroke(colorOfSelectedElement);
            });

            circRectBackground.setOnAction(e -> {
                colorOfSelectedElement = circRectBackground.getValue();
                rectangle.setFill(colorOfSelectedElement);
            });

            circRectBorderWidth.setOnKeyReleased(e -> {
                try {
                    rectangle.setStrokeWidth(Double.parseDouble(circRectBorderWidth.getText()));
                } catch (Exception ex) {

                }
            });

        } else if (selectedElement instanceof Text) {
            textProperties.setVisible(true);
            textProperties.setDisable(false);
            imageProperties.setVisible(false);
            canvasProperties.setVisible(false);
            circRectProperties.setVisible(false);
            colorOfSelectedElement = (Color) text.getFill();
            text.setStroke(Color.RED);

            textText.setOnKeyReleased(e -> {

            });

        } else if (selectedElement instanceof ImageView) {
            imageProperties.setVisible(true);
            imageProperties.setDisable(false);
            canvasProperties.setVisible(false);
            circRectProperties.setVisible(false);
            textProperties.setVisible(false);
            imageView.setStyle("-fx-opacity: 50%");

            changePath.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                Stage stage = new Stage();
                File file = fileChooser.showOpenDialog(stage);
                InputStream fileInputStream;
                try {
                    fileInputStream = new FileInputStream(file);
                    imageView.setImage(new Image(fileInputStream));
                } catch (FileNotFoundException fileNotFoundException) {

                }
            });
        }
    }

    public void unselect() {
        if (selectedElement instanceof Pane) {
            canvasProperties.setVisible(false);
            canvasProperties.setDisable(true);
        } else if (selectedElement instanceof Circle) {
            circle.setStroke(colorOfSelectedElement);
            circRectProperties.setVisible(false);
            circRectProperties.setDisable(true);
        } else if (selectedElement instanceof Rectangle) {
            rectangle.setStroke(colorOfSelectedElement);
            circRectProperties.setVisible(false);
            circRectProperties.setDisable(true);
        } else if (selectedElement instanceof Text) {
            text.setStroke(colorOfSelectedElement);
            textProperties.setVisible(false);
            textProperties.setDisable(true);
        } else if (selectedElement instanceof ImageView) {
            imageView.setStyle("-fx-opacity: 100%");
            imageProperties.setVisible(false);
            imageProperties.setDisable(true);
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
