package view.components;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import model.cards.Card;
import view.menus.RootMenu;

import java.util.Objects;

public class Assets {
    public final static Image BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button.png")).toExternalForm());
    public final static Image SELECTED_BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button-selected.png")).toExternalForm());
    public final static Image MENU_BACKGROUND = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/main-background.png")).toExternalForm());
    public final static Image UNKNOWN_CARD = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/cards/unknown.jpg")).toExternalForm());
    public final static ImageCursor UNAVAILABLE_CURSOR = new ImageCursor(new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/unavailable_cursor.png")).toExternalForm()));
    private final static Image COIN_HEADS = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/coins/heads.png")).toExternalForm());
    private final static Image COIN_TAILS = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/coins/tails.png")).toExternalForm());

    public static void setMenuBackgroundImage(Region borderPane) {
        borderPane.setBackground(new Background(new BackgroundImage(Assets.MENU_BACKGROUND, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
    }

    /**
     * Gets the image of a card from resources
     * If this couldn't find the card, it will return {@link #UNKNOWN_CARD}
     *
     * @param card The card to get the image
     * @return The card image
     */
    public static Image getCardImage(Card card) {
        try {
            return new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/cards/" + card.getName() + ".jpg")).toExternalForm());
        } catch (NullPointerException ex) {
            return UNKNOWN_CARD;
        }
    }

    public static Paint getCoinImage(int number) {
        number %= 2;
        if (number == 0)
            return new ImagePattern(COIN_HEADS);
        return new ImagePattern(COIN_TAILS);
    }

    private Assets() {
    }
}