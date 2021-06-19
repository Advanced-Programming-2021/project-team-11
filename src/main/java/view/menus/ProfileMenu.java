package view.menus;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import view.components.Assets;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileMenu implements Initializable {
    public BorderPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
    }
}
