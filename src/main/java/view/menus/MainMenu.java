package view.menus;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.User;
import view.components.Assets;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    public static User loggedInUser = null;
    public BorderPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
    }

    public void clickedScoreboardButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.SCOREBOARD);
    }

    public void clickedLogoutButton(MouseEvent mouseEvent) {
        MainMenu.loggedInUser = null;
        SceneChanger.changeScene(MenuNames.LOGIN);
    }
}
