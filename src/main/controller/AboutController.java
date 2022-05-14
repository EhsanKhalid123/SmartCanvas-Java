package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private Button close;

    @FXML
    void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

}
