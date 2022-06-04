package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    // Variable Declarations - Javafx Components Based on SceneBuilder
    @FXML
    private Button close;

    // Action Event Created Using SceneBuilder Attached to Close Button
    @FXML
    void close() {
        // Closes the current open stage/window
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

}
