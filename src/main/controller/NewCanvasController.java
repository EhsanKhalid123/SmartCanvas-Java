package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NewCanvasController {
    // Variable Declarations - Javafx Components Based on SceneBuilder
    @FXML
    private TextField canvasHeight;
    @FXML
    private TextField canvasWidth;
    @FXML
    private Button ok;
    @FXML
    private Label newCanErrMsg;

    // Variable Declarations
    private Pane borderPane;
    private Pane canvas;
    private MenuItem saveAsMenu;
    private Menu fileMenu;

    // Method used to pass elements from SmartCanvasController to this controller class
    void SMC(Pane borderPane, Pane canvas, MenuItem saveAsMenu, Menu fileMenu) {
        // Assigning passed components to this classes declared variables
        this.borderPane = borderPane;
        this.canvas = canvas;
        this.saveAsMenu = saveAsMenu;
        this.fileMenu = fileMenu;
    }

    // ActionEvent for Cancel Button Declared in Scene Builder
    @FXML
    void newCanvasCancel() {
        // Closes the window
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    // ActionEvent for Ok Button Declared in Scene Builder
    @FXML
    void newCanvasOk() {
        // If the canvas pane has elements then clear all of them before creating a new Canvas
        if (!canvas.getChildren().isEmpty()) {
            canvas.getChildren().clear();
        }

        // Gets user input for height and weight
        String height = canvasHeight.getText();
        String width = canvasWidth.getText();

        // Conditional Statement runs only if user has given height and width
        if (!height.isEmpty() || !width.isEmpty()) {
            try {
                // Setting canvas pane and border panes height and width
                canvas.setPrefHeight(Double.parseDouble(height));
                canvas.setPrefWidth(Double.parseDouble(width));
                borderPane.setPrefHeight(Double.parseDouble(height));
                borderPane.setPrefWidth(Double.parseDouble(width));
                // Colours the canvas to white
                canvas.setStyle("-fx-background-color: white");
            } catch (Exception e) {
                // Error Message
                newCanErrMsg.setText("Enter Only Numbers!");
                return;
            }

            // Removes the previous canvas
            borderPane.getChildren().remove(canvas);
            // Adds a new canvas
            borderPane.getChildren().add(canvas);

            // Canvas Properties - Adding a drop shadow
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2);
            dropShadow.setOffsetY(2);
            dropShadow.setColor(Color.rgb(107, 106, 106));
            canvas.setEffect(dropShadow);
        }

        // Enables save as option in menu if canvas exists
        if (borderPane.getChildren().contains(canvas) || (canvas.getHeight() != 0 && canvas.getWidth() != 0)) {
            saveAsMenu.setDisable(false);
        }

        // Disable save as option from menu if canvas does not exist
        fileMenu.setOnShowing(event -> {
            if (!borderPane.getChildren().contains(canvas) || (canvas.getHeight() == 0 && canvas.getWidth() == 0)) {
                saveAsMenu.setDisable(true);
            }
        });

        // Closes the window
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }
}
