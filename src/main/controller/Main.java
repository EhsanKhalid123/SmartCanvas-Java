package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception{

        scene = new Scene(openFXML("resources/views/login"));
        stage.setScene(scene);
        Main.stage = stage;
        stage.show();
        stage.setTitle("Welcome to SmartCanvas!");
        stage.show();
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/views/Whiteboard-512.png"));
        stage.getIcons().add(icon);
    }

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

    static Parent openFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
