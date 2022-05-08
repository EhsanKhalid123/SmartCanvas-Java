package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;

public class NewCanvasController {
    @FXML
    private TextField canvasLength;
    @FXML
    private TextField canvasWidth;
    @FXML
    private Button ok;

    public static String length;
    public static String width;

    @FXML
    void initialize() {
    }

    @FXML
    void newCanvasCancel() {
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    @FXML
    void newCanvasOk() {

        length = canvasLength.getText();
        width = canvasWidth.getText();

        System.out.println("length: " + length + "Width: " + width);

        if (!length.isEmpty() || !width.isEmpty()) {

            Model.pane.setMinHeight(Double.parseDouble(length));
            Model.pane.setMinWidth(Double.parseDouble(width));

            Model.canvas.setHeight(Double.parseDouble(length));
            Model.canvas.setWidth(Double.parseDouble(width));

            if (Model.pane.getChildren().contains(Model.canvas)) {
                Model.pane.getChildren().remove(Model.canvas);
            }
            Model.pane.getChildren().add(Model.canvas);
            Model.pane.setStyle("-fx-background-color: white");

            if (Model.canvasPane.getChildren().contains(Model.pane)) {
                Model.canvasPane.getChildren().remove(Model.pane);
            }
            Model.canvasPane.getChildren().add(Model.pane);
            Model.canvasPane.setStyle("-fx-background-color: white");
        }

        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

}
