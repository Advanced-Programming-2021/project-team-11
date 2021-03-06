package view.global;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import model.PlayableCard;
import model.cards.Card;
import view.menus.RootMenu;

import java.net.URL;
import java.util.Objects;

public class Assets {
    public final static Image BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button.png")).toExternalForm());
    public final static Image SELECTED_BUTTON_IMAGE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/button-selected.png")).toExternalForm());
    public final static Image MENU_BACKGROUND = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/main-background.png")).toExternalForm());
    public final static Image UNKNOWN_CARD = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/cards/unknown.jpg")).toExternalForm());
    public final static ImageCursor UNAVAILABLE_CURSOR = new ImageCursor(new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/unavailable_cursor.png")).toExternalForm()));
    private final static Image COIN_HEADS = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/coins/heads.png")).toExternalForm());
    private final static Image COIN_TAILS = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/coins/tails.png")).toExternalForm());
    private final static Image NORMAL_FIELD = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/fields/Normal.bmp")).toExternalForm());
    public final static Image SWORD = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/sword.png")).toExternalForm());
    public final static Image DEATH = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/death.png")).toExternalForm());
    public final static Image CHECKMARK = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/checkmark.png")).toExternalForm());
    public final static Image REVIVE = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/revive.png")).toExternalForm());
    public final static Image HEARTBLEED = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/heartbleed.png")).toExternalForm());
    public final static Image TORNADO = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/tornado.png")).toExternalForm());
    public final static Image RADAR = new Image(Objects.requireNonNull(RootMenu.class.getResource("/assets/radar.png")).toExternalForm());
    public final static Media MUSIC = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/music.mp3")).toExternalForm());

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

    public static Image getFieldImage(PlayableCard field) {
        if (field == null)
            return NORMAL_FIELD;
        String cardName = field.getCard().getName();
        URL image = RootMenu.class.getResource(String.format("/assets/fields/%s.bmp", cardName));
        if (image == null)
            return NORMAL_FIELD;
        return new Image(image.toExternalForm());
    }

    private Assets() {
    }
}