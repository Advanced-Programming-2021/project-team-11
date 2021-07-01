package view.menus;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.User;
import view.components.Assets;
import view.components.UserBadge;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    public static User loggedInUser = null;
    @FXML
    private BorderPane rootPane;
    @FXML
    private UserBadge userBadge;

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

    public void clickedShopButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.SHOP);
    }

    public void clickedImportExportButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.IMPORT_EXPORT);
    }
}
