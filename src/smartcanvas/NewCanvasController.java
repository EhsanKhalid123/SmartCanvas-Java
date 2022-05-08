package smartcanvas;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            UserDao.pane.setMinHeight(Double.parseDouble(length));
            UserDao.pane.setMinWidth(Double.parseDouble(width));

            UserDao.canvas.setHeight(Double.parseDouble(length));
            UserDao.canvas.setWidth(Double.parseDouble(width));


            if (UserDao.pane.getChildren().contains(UserDao.canvas)) {
                UserDao.pane.getChildren().remove(UserDao.canvas);
            }
            UserDao.pane.getChildren().add(UserDao.canvas);
            UserDao.pane.setStyle("-fx-background-color: white");

            if (UserDao.canvasPane.getChildren().contains(UserDao.pane)) {
                UserDao.canvasPane.getChildren().remove(UserDao.pane);
            }
            UserDao.canvasPane.getChildren().add(UserDao.pane);
            UserDao.canvasPane.setStyle("-fx-background-color: white");
        }

        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

}
