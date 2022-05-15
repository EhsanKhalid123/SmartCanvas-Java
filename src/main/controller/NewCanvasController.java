package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NewCanvasController {
    @FXML
    private TextField canvasHeight;
    @FXML
    private TextField canvasWidth;
    @FXML
    private Button ok;

    private Pane borderPane;
    private StackPane canvas;
    private MenuItem saveAsMenu;
    public static String height;
    public static String width;


    @FXML
    void initialize() {
    }

    void SMC(Pane borderPane, StackPane canvas, MenuItem saveAsMenu) {
        this.borderPane = borderPane;
        this.canvas = canvas;
        this.saveAsMenu = saveAsMenu;
    }

    @FXML
    void newCanvasCancel() {
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    @FXML
    void newCanvasOk() {
        if (!canvas.getChildren().isEmpty()) {
            canvas.getChildren().clear();
        }
        height = canvasHeight.getText();
        width = canvasWidth.getText();

        if (!height.isEmpty() || !width.isEmpty()) {
            canvas.setMinSize(Double.parseDouble(height), Double.parseDouble(width));
            canvas.setStyle("-fx-background-color: white");

            if (borderPane.getChildren().contains(canvas)) {
                borderPane.getChildren().remove(canvas);
            }
            borderPane.getChildren().add(canvas);
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2);
            dropShadow.setOffsetY(2);
            dropShadow.setColor(Color.rgb(107, 106, 106));
            borderPane.setEffect(dropShadow);
        }

//        todo Ask Why is this working opposite to what I am asking
//          when I write 1920 1080 then the button disables unless i write 0 0 then 1920 1080 and why height and length
//          mixed up for when saving picture and for when viewing in software

        if (canvas.getHeight() == 0 && canvas.getWidth() == 0) {
            saveAsMenu.setDisable(false);
        } else {
            saveAsMenu.setDisable(true);
        }

        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();



    }

}
