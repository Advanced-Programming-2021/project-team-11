package view.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.User;

import java.io.IOException;

public class UserBadge extends HBox {
    public Circle profilePic;
    public Text username;
    public Text coins;

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
        username.setText(user.getUsername());
        coins.setText(String.valueOf(user.getMoney()));
    }
}
