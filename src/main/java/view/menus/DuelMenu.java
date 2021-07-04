package view.menus;

import controller.GameController;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class DuelMenu implements Initializable {
    public static GameController gameController;
    public static User player1, player2;
    public ImageView fieldImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
