package view.menus;

import controller.menucontrollers.LoginMenuController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.exceptions.NetworkErrorException;
import view.components.AlertsUtil;
import view.global.Assets;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterMenu implements Initializable {
    private static boolean doingTask = false;
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
        if (doingTask)
            return;
        doingTask = true;
        new Thread(() -> {
            try {
                LoginMenuController.register(username.getText(), password.getText(), passwordConfirm.getText(), nickname.getText());
                Platform.runLater(() -> {
                    AlertsUtil.showSuccess("You have been registered!");
                    clickedBackButton(null);
                });
            } catch (NetworkErrorException e) {
                Platform.runLater(() -> AlertsUtil.showError(e));
            } finally {
                doingTask = false;
            }
        }).start();
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.ROOT);
    }
}
