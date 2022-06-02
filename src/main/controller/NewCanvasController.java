package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
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
    private Pane canvas;
    private MenuItem saveAsMenu;
    private String height;
    private String width;
    private Menu fileMenu;


    @FXML
    void initialize() {

    }

    void SMC(Pane borderPane, Pane canvas, MenuItem saveAsMenu, Menu fileMenu) {
        this.borderPane = borderPane;
        this.canvas = canvas;
        this.saveAsMenu = saveAsMenu;
        this.fileMenu = fileMenu;
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
            canvas.setPrefWidth(Double.parseDouble(width));
            canvas.setPrefHeight(Double.parseDouble(height));
            borderPane.setPrefHeight(Double.parseDouble(height));
            borderPane.setPrefWidth(Double.parseDouble(width));
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

        if (borderPane.getChildren().contains(canvas) || (canvas.getHeight() != 0 && canvas.getWidth() != 0)) {
            saveAsMenu.setDisable(false);
        }

        fileMenu.setOnShowing(event -> {
            if (borderPane.getChildren().contains(canvas) == false || (canvas.getHeight() == 0 && canvas.getWidth() == 0)) {
                saveAsMenu.setDisable(true);
            }
        });

        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();

    }

}
