package view.components;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import view.menus.RootMenu;

import java.util.Objects;

public class Assets {
    public final static Image BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button.png")).toExternalForm());
    public final static Image SELECTED_BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button-selected.png")).toExternalForm());
    public final static Image MENU_BACKGROUND = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/main-background.png")).toExternalForm());

    public static void setMenuBackgroundImage(Region borderPane) {
        borderPane.setBackground(new Background(new BackgroundImage(Assets.MENU_BACKGROUND, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
    }

    private Assets() {
    }
}