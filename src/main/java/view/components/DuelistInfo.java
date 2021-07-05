package view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Player;
import view.util.ViewUtil;

import java.io.IOException;

public class DuelistInfo extends HBox {
    @FXML
    private Circle profilePic;
    @FXML
    private Text nickname;
    @FXML
    private Text username;
    @FXML
    private Text lp;

    public DuelistInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("duelist_info.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.setClassLoader(getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(Player player) {
        profilePic.setFill(new ImagePattern(player.getUser().getProfilePicImage()));
        username.setText(ViewUtil.beautyPrintText(player.getUser().getUsername(), 17));
        nickname.setText(ViewUtil.beautyPrintText(player.getUser().getNickname(), 17));
        lp.setText("LP: " + player.getHealth());
    }
}
