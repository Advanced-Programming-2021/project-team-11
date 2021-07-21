package view.menus;

import controller.menucontrollers.LoginMenuController;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.User;
import view.global.Assets;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoDialog implements Initializable {
    public static String username;
    public BorderPane background;
    public ImageView profilePic;
    public Label usernameLabel;
    public Label nicknameLabel;
    public Label scoreLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(background);
        User user = LoginMenuController.getUserByUsername(username);
        if (user == null)
            return;
        profilePic.setImage(user.getProfilePicImage());
        usernameLabel.setText("Username: " + user.getUsername());
        nicknameLabel.setText("Nickname: " + user.getNickname());
        scoreLabel.setText("Score: " + user.getScore());
    }
}
