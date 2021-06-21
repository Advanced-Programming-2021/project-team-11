package view.menus;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.User;
import view.components.Assets;
import view.components.UserBadge;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    public static User loggedInUser = null;
    public BorderPane rootPane;
    public UserBadge userBadge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
        userBadge.setUser(loggedInUser);
    }

    public void clickedScoreboardButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.SCOREBOARD);
    }

    public void clickedProfileButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.PROFILE);
    }

    public void clickedLogoutButton(MouseEvent mouseEvent) {
        MainMenu.loggedInUser = null;
        SceneChanger.changeScene(MenuNames.LOGIN);
    }
}
