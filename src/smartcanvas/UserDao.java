package smartcanvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static List<User> users = new ArrayList<>();
    public static List<String> loggedUser = new ArrayList<>();
    public static Canvas canvas = new Canvas();
    public static Pane canvasPane = new Pane();
    public static Pane pane = new Pane();

    public static List<User> getUsers(){



        return users;
    }

}
