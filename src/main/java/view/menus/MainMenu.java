package view.menus;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.User;
import view.global.Assets;
import view.components.UserBadge;
import view.global.SoundEffects;

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
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.SCOREBOARD);
    }

    public void clickedProfileButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.PROFILE);
    }

    public void clickedLogoutButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        MainMenu.loggedInUser = null;
        SceneChanger.changeScene(MenuNames.LOGIN);
    }

    public void clickedShopButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.SHOP);
    }

    public void clickedImportExportButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.IMPORT_EXPORT);
    }

    public void clickedDecksButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.DECK);
    }

    public void clickedDuelButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.DUEL_START);
    }
}
