package view.menus;

import controller.menucontrollers.LoginMenuController;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.exceptions.InvalidCredentialException;
import view.components.AlertsUtil;
import view.global.Assets;
import view.global.SoundEffects;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginMenu implements Initializable {
    public TextField username;
    public PasswordField password;
    public BorderPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.ROOT);
    }

    public void clickedLoginButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        try {
            MainMenu.loggedInUser = LoginMenuController.login(username.getText(), password.getText());
            SceneChanger.changeScene(MenuNames.MAIN);
        } catch (InvalidCredentialException e) {
            AlertsUtil.showError(e);
        }
    }
}
