package view.components;

import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import view.menus.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JfxCursorButton extends JFXButton implements Initializable {
    public JfxCursorButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("jfx_cursor_button.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.setClassLoader(getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JfxCursorButton(String text, EventHandler<? super MouseEvent> onClicked) {
        this();
        super.setText(text);
        super.setOnMouseClicked(onClicked);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Cursor.HAND));
        setOnMouseExited(x -> SceneChanger.getScene().setCursor(Cursor.DEFAULT));
    }
}
