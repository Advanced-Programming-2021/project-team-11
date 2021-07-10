package view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.User;
import view.util.ViewUtil;

import java.io.IOException;

public class UserBadge extends HBox {
    @FXML
    private Circle profilePic;
    @FXML
    private Text username;
    @FXML
    private Text coins;

    public UserBadge() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user_badge.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.setClassLoader(getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(User user) {
        profilePic.setFill(new ImagePattern(user.getProfilePicImage()));
        username.setText(ViewUtil.beautyPrintText(user.getUsername(), 17));
        coins.setText(String.valueOf(user.getMoney()));
    }
}
