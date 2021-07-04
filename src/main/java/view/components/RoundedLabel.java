package view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class RoundedLabel extends HBox {
    @FXML
    private Text text;

    public RoundedLabel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rounded_label.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.setClassLoader(getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getText() {
        return this.text.getText();
    }
}
