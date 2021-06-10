package view.components;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import view.menus.RootMenu;

import java.util.Objects;

public class Assets {
    public final static Image BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button.png")).toExternalForm());
    public final static Image SELECTED_BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button-selected.png")).toExternalForm());

    private Assets() {
    }
}