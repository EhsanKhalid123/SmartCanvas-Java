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
import javafx.scene.shape.Shape;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import model.User;

import javax.imageio.ImageIO;
import java.io.*;
import java.sql.SQLException;
import java.util.Objects;

public class SmartCanvasController {
    // Variable Declarations - Javafx Components Based on SceneBuilder
    @FXML
    private Button Bold;
    @FXML
    private Button Italics;
    @FXML
    private Pane borderPane;
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
    private TextField imageLength;
    @FXML
    private ToolBar imageProperties;
    @FXML
    private TextField imageRotate;
    @FXML
    private TextField imageWidth;
    @FXML
    private Button leftAlign;
    @FXML
    private Label nodeInfo;
    @FXML
    private Button rightAlign;
    @FXML
    private MenuItem saveAsMenu;
    @FXML
    private TextField shapeHeight;
    @FXML
    private TextField shapeRotate;
    @FXML
    private TextField shapeWidth;
    @FXML
    private TextField shapeRadius;
    @FXML
    private ColorPicker textBackgroundColour;
    @FXML
    private ColorPicker textStrokeColour;
    @FXML
    private TextField textStrokeWidth;
    @FXML
    private ColorPicker textColour;
    @FXML
    private ChoiceBox<String> textFont;
    @FXML
    private TextField textHeight;
    @FXML
    private ToolBar textProperties;
    @FXML
    private TextField textRotate;
    @FXML
    private TextField textText;
    @FXML
    private TextField textWidth;
    @FXML
    private Text userName;
    @FXML
    private Label zoomPercentage;
    @FXML
    private Slider zoomSlider;

    // Variable Declarations
    private final Pane canvas = new Pane();
    private Node selectedElement;
    private Color colorOfSelectedElement;
    private Text text;
    private Circle circle;
    private Rectangle rectangle;
    private ImageView imageView;
    // Original coordinates of the node before each dragging
    private double originX;
    private double originY;

    // Initialize Method That is run on the instance this controller runs
    @FXML
    public void initialize() throws SQLException {
        // Gets User Details and Displays in The Logged in profile Menu
        getDetails();
        // Sets Pane Zoom to 100
        setPaneZoom(100);
        // Initializes zoom and node details and sets them to 0 to begin with
        zoomSlider.setValue(100);
        nodeInfo.setText(String.format("x: %.0f y: %.0f w: %.0f h: %.0f angle: %.0f°", 0.00 + 0.00, 0.00 +
                0.00, 0.00, 0.00, 0.00));

        // Declaration of Fonts
        String[] fontFamily = {"Helvetica", "Calibri", "Times New Roman", "Verdana"};
        textFont.getItems().addAll(fontFamily);

        // OnShowing Action Event
        fileMenu.setOnShowing(event -> {
            // Disables the clear Canvas Menu Button if canvas is empty
            if (canvas.getChildren().isEmpty()) {
                clearCanvasMenu.setDisable(true);
            }
            // Disables the Save as Menu Button if canvas doesn't exist
            if (!borderPane.getChildren().contains(canvas)) {
                saveAsMenu.setDisable(true);
            }
        });

        // OnShowing Action Event
        editMenu.setOnShowing(event -> {
            // Disables the delete menu button if no node is selected
            if (selectedElement == null) {
                deleteMenu.setDisable(true);
            }
        });
    }

    // Get Logged in Users detail method
    public void getDetails() throws SQLException {
        Model model = new Model();
        User user;
        // Gets the user details from DB from the logged in username
        user = model.getUserDao().getUser(Model.loggedUser);
        // Sets the user Details to display in the menu
        userName.setText(user.getFirstname() + " " + user.getLastname());
        Image image = user.getDp();
        dpImage.setImage(image);
    }

    // ActionEvent for Logout Button Declared in Scene Builder
    @FXML
    void logout() throws IOException {
        // Empties loggedIn User Variable
        Model.loggedUser = "";
        // Removes the canvas
        borderPane.getChildren().remove(canvas);
        // Changes screen to login page
        Main.setWindow("resources/views/Login", 481, 460, "Welcome to SmartCanvas!", false);
    }

    // ActionEvent for View Profile Button Declared in Scene Builder
    @FXML
    void viewProfile() throws IOException {
        // Steps to Create a new Window
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Profile.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController pc = fxmlLoader.getController();
        // Passes the following components from this controller to the profileController to update View instantly
        pc.SMC(userName, dpImage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit profile");
        stage.setScene(new Scene(root, 430, 268));
        stage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/views/Whiteboard-512.png")));
        stage.getIcons().add(icon);
        stage.showAndWait();
    }

    // ActionEvent for About Menu Button Declared in Scene Builder
    @FXML
    void aboutMenu() throws IOException {
        // Displays a New Popup Window Showing Application Details
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/About.fxml")));
        stage.setTitle("About | Smart Canvas");
        stage.setScene(new Scene(root, 430, 299));
        stage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/views/Whiteboard-512.png")));
        stage.getIcons().add(icon);
        stage.show();
    }

    // ActionEvent for New Canvas Button Declared in Scene Builder
    @FXML
    void addCanvas() throws IOException {
        // Creates a new popup for creating a canvas
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Model.class.getResource("/views/NewCanvas.fxml"));
        Parent root = fxmlLoader.load();
        NewCanvasController ncc = fxmlLoader.getController();
        // Passes the elements to the NewCanvasController for instant view Update
        ncc.SMC(borderPane, canvas, saveAsMenu, fileMenu);
        stage.setTitle("Create a new canvas");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, 300, 145));
        stage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(Model.class.getResourceAsStream("/views/Whiteboard-512.png")));
        stage.getIcons().add(icon);
        stage.showAndWait();
    }

    // ActionEvent for Zoom Slider Declared in Scene Builder
    @FXML
    void zoom() {
        // Event Listener to update the zoom value on the label
        zoomSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
                setPaneZoom(newValue.intValue()));
    }

    private void setPaneZoom(int percentage) {
        // Sets Pane Scale
        canvas.setScaleX(percentage / 100.0);
        canvas.setScaleY(percentage / 100.0);
        // Updates the Zoom Value
        zoomPercentage.setText(percentage + "%");
    }

    // ActionEvent for Add Image Button Declared in Scene Builder
    @FXML
    void addImage() {
        // If canvas exists then create an image and display on canvas
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            Stage stage = new Stage();
            imageView = new ImageView();
            if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
                // Add imageView node on canvas if canvas has height and width
                canvas.getChildren().add(imageView);
            }

            // Object Creation
            FileChooser fileChooser = fileChooserExt();

            // Show a file open dialog
            File file = fileChooser.showOpenDialog(stage);

            InputStream fileInputStream;
            // ImageView Properties
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            try {
                // FIS Creation and Sets Image in the Image View
                fileInputStream = new FileInputStream(file);
                imageView.setImage(new Image(fileInputStream));
                // Clear Canvas Menu is Enabled
                clearCanvasMenu.setDisable(false);
            } catch (Exception e) {
                // Exception Handling
                System.out.println("No Image Selected!");
            }
            // Methods for Action Events for Moving and Selecting Elements
            calcCoordinates(imageView);
            selectEvent(imageView);
        }
    }

    // ActionEvent for Add Rectangle Button Declared in Scene Builder
    @FXML
    void addRectangle() {
        // Creates and Adds Rectangle on Canvas
        rectangle = new Rectangle(100, 100, 100, 100);
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(rectangle);
        }
        // Rectangle Properties
        rectangle.setFill(Color.valueOf("#1bbcd1"));
        // Enables Clear Canvas Menu
        clearCanvasMenu.setDisable(false);
        // Methods for Action Events for Moving and Selecting Elements
        calcCoordinates(rectangle);
        selectEvent(rectangle);
    }

    // ActionEvent for Add Circle Button Declared in Scene Builder
    @FXML
    void addCircle() {
        // Creates a Circle and Adds to Canvas
        circle = new Circle(70, 70, 50);
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(circle);
        }
        // Circle Setting Colour
        circle.setFill(Color.valueOf("#0ec943"));
        // Enables Clear Canvas Menu
        clearCanvasMenu.setDisable(false);
        // Methods for Action Events for Moving and Selecting Elements
        calcCoordinates(circle);
        selectEvent(circle);
    }

    // ActionEvent for Add Text Button Declared in Scene Builder
    @FXML
    void addText() {
        // Creates a Text and adds to Canvas
        text = new Text(20, 20, "Text");
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            canvas.getChildren().add(text);
        }
        // Text Properties Setting
        text.setFont(Font.font(12));
        text.setWrappingWidth(255);
        // Enables Clear Canvas Menu
        clearCanvasMenu.setDisable(false);
        // Methods for Action Events for Moving and Selecting Elements
        calcCoordinates(text);
        selectEvent(text);
    }

    // ActionEvent for Clear Canvas Button Declared in Scene Builder
    @FXML
    void clearCanvas() {
        // If canvas is not empty then do the following
        if (!canvas.getChildren().isEmpty()) {
            // Removes all the elements from the canvas
            canvas.getChildren().clear();
            // Disabled Clear Canvas Button
            clearCanvasMenu.setDisable(true);
            // Sets node Info to 0
            nodeInfo.setText(String.format("x: %.0f y: %.0f w: %.0f h: %.0f angle: %.0f°", 0.00 + 0.00, 0.00 +
                    0.00, 0.00, 0.00, 0.00));
            // Hides all the side property panels
            deleteMenu.setDisable(true);
            canvasProperties.setVisible(false);
            circRectProperties.setVisible(false);
            textProperties.setVisible(false);
            imageProperties.setVisible(false);
        }
    }

    // ActionEvent for Save as Button Declared in Scene Builder
    @FXML
    void saveAs() {
        // If canvas exists
        if (canvas.getHeight() != 0 && canvas.getWidth() != 0) {
            // Take a snapshot of the canvas pane
            WritableImage wim = new WritableImage((int) canvas.getHeight(), (int) canvas.getWidth());
            canvas.snapshot(null, wim);

            // Declares file saving extensions - calls the fileChooseExt Method
            FileChooser fileChooser = fileChooserExt();

            Stage stage = new Stage();

            // Opens save Dialog
            File file = fileChooser.showSaveDialog(stage);
            try {
                // Saves image to drive
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
            } catch (Exception ignored) {
            }
        }
    }

    // ActionEvent for Canvas Button Declared in Scene Builder
    @FXML
    void colourCanvas() {
        // If canvas Exists
        if (borderPane.getChildren().contains(canvas)) {
            // If pane is not visible then set it to visible and hide all other panes
            if (!canvasProperties.isVisible()) {
                canvasProperties.setVisible(true);
                circRectProperties.setVisible(false);
                textProperties.setVisible(false);
                imageProperties.setVisible(false);
                // Select the Canvas
                selectedElement = canvas;
                // Colour Picker Action Event
                canvasColourPicker.setOnAction(e -> {
                    // Assigns the Canvas the selected colour from the colour picker
                    if (selectedElement == canvas) {
                        colorOfSelectedElement = canvasColourPicker.getValue();
                        ((Pane) selectedElement).setBackground(new Background(
                                new BackgroundFill(colorOfSelectedElement, null, null)));
                    }
                });
                // If properties pane is visible set it to hidden with all other panes
            } else if (canvasProperties.isVisible()) {
                canvasProperties.setVisible(false);
                circRectProperties.setVisible(false);
                textProperties.setVisible(false);
                imageProperties.setVisible(false);
                // Unselect the canvas
                selectedElement = null;
            }
        }
    }

    // ActionEvent for Delete Menu Button Declared in Scene Builder
    @FXML
    void deleteButton() {
        // Delete the selected element from the canvas
        canvas.getChildren().remove(selectedElement);
        // Hide all node properties pane since no node is selected
        canvasProperties.setVisible(false);
        circRectProperties.setVisible(false);
        textProperties.setVisible(false);
        imageProperties.setVisible(false);
        // Disable the delete menu since no node is selected to be deleted
        deleteMenu.setDisable(true);
        if (canvas.getChildren().isEmpty()) {
            // Disables clear Canvas Menu option if canvas is empty
            clearCanvasMenu.setDisable(true);
        }
    }

    // Calculates node coordinates on the canvas
    private void calcCoordinates(Node node) {
        // Gets original X and Y value of the node
        node.setOnMousePressed(e -> {
            originX = e.getX();
            originY = e.getY();
        });

        // Move the node by dragging
        node.setOnMouseDragged(e -> {
            double dx = e.getX() - originX;
            double dy = e.getY() - originY;
            move(node, dx, dy);
        });
    }

    // Selects the Elements
    public void selectEvent(Node node) {
        // Action Event
        node.setOnMouseClicked(e -> {
            // Unselects previously selected elements
            unselect();
            // Selects element that has been passed in parameters
            select(node);
            // Node is in Focus
            node.requestFocus();
        });
    }

    // Select Method for selecting Nodes
    public void select(Node node) {
        // Selects a Node
        selectedElement = node;
        // Allows individual elements to be deleted
        deleteMenu.setDisable(false);

        // Conditional Statements for certain types of Nodes
        if (selectedElement instanceof Pane) {
            // Enables canvas properties panel and hides all the other panels
            canvasProperties.setVisible(true);
            canvasProperties.setDisable(false);
            circRectProperties.setVisible(false);
            textProperties.setVisible(false);
            imageProperties.setVisible(false);

        } else if (selectedElement instanceof Circle) {
            // Selects the Circle and Allows User to Edit the Circle
            shapeProperties(circle);
            // Action Event to Change the radius of the circle
            shapeRadius.setOnKeyReleased(e -> {
                try {
                    // Changes the radius to the user input request
                    circle.setRadius(Double.parseDouble(shapeRadius.getText()));
                } catch (Exception ignored) {
                }
            });
        } else if (selectedElement instanceof Rectangle) {
            // Allows users to edit rectangle properties if selected
            shapeProperties(rectangle);
        } else if (selectedElement instanceof Text) {
            // Allows users to edit text properties if selected
            textProperties();
        } else if (selectedElement instanceof ImageView) {
            // Allows users to edit image properties if selected
            imageProperties();
        }
    }

    // Unselect Method - Unselects Node Changes the Red selected node to the colour of the node
    public void unselect() {
        if (selectedElement instanceof Pane) {
            // Hide Canvas Properties Panel if Unselected
            canvasProperties.setVisible(false);
        } else if (selectedElement instanceof Circle) {
            circle.setStroke(colorOfSelectedElement);
            // Hide Circle Properties Panel if Unselected
            circRectProperties.setVisible(false);
        } else if (selectedElement instanceof Rectangle) {
            rectangle.setStroke(colorOfSelectedElement);
            // Hide Rectangle Properties Panel if Unselected
            circRectProperties.setVisible(false);
        } else if (selectedElement instanceof Text) {
            text.setStroke(colorOfSelectedElement);
            // Hide Text Properties Panel if Unselected
            textProperties.setVisible(false);
        } else if (selectedElement instanceof ImageView) {
            imageView.setStyle("-fx-opacity: 100%");
            // Hide Image Properties Panel if Unselected
            imageProperties.setVisible(false);
        }
        // Unselects selected node
        selectedElement = null;
    }

    // Move Method
    private void move(Node node, double dx, double dy) {
        // Gets node X, Y, Height and Width and Rotation
        double x = node.getBoundsInParent().getMinX();
        double y = node.getBoundsInParent().getMinY();
        double h = node.getBoundsInParent().getHeight();
        double w = node.getBoundsInParent().getWidth();
        double r = node.getRotate();
        // Displays the properties in the bottom status bar
        nodeInfo.setText(String.format("x: %.0f y: %.0f w: %.0f h: %.0f angle: %.0f°", x + dx, y + dy, w, h, r));
        // Moves the node to new positions
        node.relocate(x + dx, y + dy);
    }

    // REFERENCE: https://www.javafixing.com/2022/01/fixed-how-to-convert-color-from.html
    // Converts String Colour Characters to Hex Code
    private static String hexConvert(Color color) {
        int r = ((int) Math.round(color.getRed() * 255)) << 24;
        int g = ((int) Math.round(color.getGreen() * 255)) << 16;
        int b = ((int) Math.round(color.getBlue() * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("#%08X", (r + g + b + a));
    }

    // Method to select file Extensions when selecting and saving images
    private FileChooser fileChooserExt() {
        FileChooser fileChooser = new FileChooser();
        // All the Following File Extensions are Allowed
        ExtensionFilter bitmapFilter = new FileChooser.ExtensionFilter("Bitmap Files (*.bmp;*.dib)",
                "*.bmp", "*.dib");
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

        // Adds all the file Extensions to the fileChooser
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

        // Returns the fileChooser
        return fileChooser;
    }

    // Event Handlers to Modify Rectangle and Circle
    public void shapeProperties(Shape shape) {
        // Enables property panel
        circRectProperties.setVisible(true);
        canvasProperties.setVisible(false);
        textProperties.setVisible(false);
        imageProperties.setVisible(false);
        // Gets the current colour of the shape
        colorOfSelectedElement = (Color) shape.getFill();
        // Sets the stroke of the shape to red to denote it is selected
        shape.setStroke(Color.RED);

        // Changes the colour of the stroke
        circRectColourPicker.setOnAction(e -> {
            colorOfSelectedElement = circRectColourPicker.getValue();
            shape.setStroke(colorOfSelectedElement);
        });

        // Changes the colour of the shape
        circRectBackground.setOnAction(e -> {
            colorOfSelectedElement = circRectBackground.getValue();
            shape.setFill(colorOfSelectedElement);
        });

        // Changes the size of the stroke
        circRectBorderWidth.setOnKeyReleased(e -> {
            try {
                shape.setStrokeWidth(Double.parseDouble(circRectBorderWidth.getText()));
            } catch (Exception ignored) {
            }
        });

        // Changes the Height and Width and Rotation Property of the Shape
        heightWidthRotate(shapeHeight, shape, shapeWidth, shapeRotate);
    }

    // Event Handlers to Modify Text
    private void textProperties() {
        // Enables property panel
        textProperties.setVisible(true);
        imageProperties.setVisible(false);
        canvasProperties.setVisible(false);
        circRectProperties.setVisible(false);
        // Gets the current colour of the shape
        colorOfSelectedElement = (Color) text.getFill();
        // Sets the stroke of the shape to red to denote it is selected
        text.setStroke(Color.RED);

        // Changes the text to user input request
        textText.setOnKeyReleased(e ->
                text.setText(textText.getText()));

        // Changes the font size of the text
        fontSize.setOnKeyReleased(e -> {
            try {
                text.setFont(Font.font(null, null, null, Double.parseDouble(fontSize.getText())));
            } catch (Exception ignored) {
            }
        });

        // Adds fonts to dropdown box
        String fontFam = textFont.getValue();
        // Sets text font
        textFont.setOnAction(e ->
                text.setFont(Font.font(fontFam, text.getFont().getSize())));

        // Makes the text Bold
        Bold.setOnAction(e ->
                text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, null, text.getFont().getSize())));

        // Makes the text Italics
        Italics.setOnAction(e ->
                text.setFont(Font.font(text.getFont().getFamily(), FontPosture.ITALIC, text.getFont().getSize())));

        // Changes the Text Colour
        textColour.setOnAction(e -> {
            colorOfSelectedElement = textColour.getValue();
            text.setFill(colorOfSelectedElement);
        });

        // Changes the Text Strike Colour
        textStrokeColour.setOnAction(e -> {
            colorOfSelectedElement = textStrokeColour.getValue();
            text.setStroke(colorOfSelectedElement);
        });

        // Changes the Stroke Size
        textStrokeWidth.setOnKeyReleased(e -> {
            try {
                text.setStrokeWidth(Double.parseDouble(textStrokeWidth.getText()));
            } catch (Exception ignored) {
            }
        });

        // Changes the Text Background Colour
        textBackgroundColour.setOnAction(e -> {
            colorOfSelectedElement = textBackgroundColour.getValue();
            text.setStyle("\n" + "-fx-background-color: " + '"' + hexConvert(colorOfSelectedElement) + '"' + ";");
        });

        // Aligns text to left
        leftAlign.setOnAction(e ->
                text.setTextAlignment(TextAlignment.LEFT));
        // Aligns text to Center
        centerAlign.setOnAction(e ->
                text.setTextAlignment(TextAlignment.CENTER));
        // Aligns text to Right
        rightAlign.setOnAction(e ->
                text.setTextAlignment(TextAlignment.RIGHT));

        // Changes the Height and Width and Rotation Property of the Shape
        heightWidthRotate(textHeight, text, textWidth, textRotate);
    }

    // Event Handlers to Modify Image
    private void imageProperties() {
        // Enables property panel
        imageProperties.setVisible(true);
        canvasProperties.setVisible(false);
        circRectProperties.setVisible(false);
        textProperties.setVisible(false);
        // Lowers the opacity of the image to show the selection of image
        imageView.setStyle("-fx-opacity: 70%");

        // Changes the selected images path so changes the image
        changePath.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            Stage stage = new Stage();
            File file = fileChooser.showOpenDialog(stage);
            InputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
                imageView.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException ignored) {
            }
        });

        // Changes the Height and Width and Rotation Property of the Shape
        heightWidthRotate(imageLength, imageView, imageWidth, imageRotate);
    }

    // Event Handlers to Modify height, width and rotation for all nodes - Common Method for all nodes
    private void heightWidthRotate(TextField nodeHeight, Node node, TextField nodeWidth, TextField nodeRotate) {
        // Resizes the height of the selected node
        nodeHeight.setOnKeyReleased(e -> {
            try {
                node.setScaleY(Double.parseDouble(nodeHeight.getText()));
            } catch (Exception ignored) {
            }
        });

        // Resizes the Width of the selected node
        nodeWidth.setOnKeyReleased(e -> {
            try {
                node.setScaleX(Double.parseDouble(nodeWidth.getText()));
            } catch (Exception ignored) {
            }
        });

        // Rotates the selected node
        nodeRotate.setOnKeyReleased(e -> {
            try {
                node.setRotate(Double.parseDouble(nodeRotate.getText()));
            } catch (Exception ignored) {
            }
        });
    }

}
