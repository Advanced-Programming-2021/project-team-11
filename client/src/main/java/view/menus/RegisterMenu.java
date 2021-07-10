package view.menus;

import controller.menucontrollers.LoginMenuController;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.exceptions.NicknameExistsException;
import model.exceptions.PasswordsDontMatchException;
import model.exceptions.UsernameExistsException;
import view.components.AlertsUtil;
import view.global.Assets;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterMenu implements Initializable {
    public TextField username;
    public PasswordField password;
    public PasswordField passwordConfirm;
    public BorderPane rootPane;
    public TextField nickname;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
    }

    public void clickedRegisterButton(MouseEvent mouseEvent) {
        if (username.getText().isEmpty() || password.getText().isEmpty() || passwordConfirm.getText().isEmpty() || nickname.getText().isEmpty())
            return;
        try {
            LoginMenuController.register(username.getText(), password.getText(), passwordConfirm.getText(), nickname.getText());
            AlertsUtil.showSuccess("You have been registered!");
            clickedBackButton(null);
        } catch (UsernameExistsException | NicknameExistsException | PasswordsDontMatchException e) {
            AlertsUtil.showError(e);
        }
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.ROOT);
    }
}
