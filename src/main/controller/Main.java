package controller;

import dao.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    // Variable Declarations
    private static Scene scene;
    private static Stage stage;
    private Model model;

    @Override
    public void init() {
        // Creating Objects as the program starts
        model = new Model();
        Database database = new Database();
        // Creates database
        database.createDatabase();
    }

    // JavaFx default method for starting the application
    @Override
    public void start(Stage stage) throws Exception {
        // Creates a table in DB
        model.setup();

        // Steps to Create a Window
        scene = new Scene(openFXML("resources/views/login"));
        stage.setScene(scene);
        Main.stage = stage;
        stage.show();
        stage.setTitle("Welcome to SmartCanvas!");
        stage.show();
        stage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/views/Whiteboard-512.png")));
        stage.getIcons().add(icon);
    }

    // Method to easily switch to different windows
    static void setWindow(String fxml, int width, int height, String title, boolean resize) throws IOException {
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setMaximized(false);
        stage.setTitle(title);
        stage.setResizable(resize);
        scene.setRoot(openFXML(fxml));
    }

    // Loads fxml files, easy way rather than creating a whole new Window
    static Parent openFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Main Method
    public static void main(String[] args) {
        launch(args);
    }
}
