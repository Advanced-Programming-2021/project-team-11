package view.components;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import view.menus.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CursorImage extends ImageView implements Initializable {
    public CursorImage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cursor_image.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.setClassLoader(getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Cursor.HAND));
        setOnMouseExited(x -> SceneChanger.getScene().setCursor(Cursor.DEFAULT));
    }
}
