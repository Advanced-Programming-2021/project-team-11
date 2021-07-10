package view.global;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import view.menus.RootMenu;

import java.util.Objects;

public class SoundEffects {
    public final static Media HOVER = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/hover.mp3")).toExternalForm());
    public final static Media CLICK = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/click.mp3")).toExternalForm());
    public final static Media DIRECT_ATTACK = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/direct_attack.mp3")).toExternalForm());
    public final static Media ATTACK = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/attack.mp3")).toExternalForm());
    public final static Media COIN_FLIP = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/coinflip.mp3")).toExternalForm());
    public final static Media DRAW = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/draw.mp3")).toExternalForm());
    public final static Media CARD_FLIP = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/flip.mp3")).toExternalForm());
    public final static Media CARD_BOUGHT = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/shop.mp3")).toExternalForm());
    public final static Media DUEL_END = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/duel_end.mp3")).toExternalForm());
    public final static Media SET = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/set.mp3")).toExternalForm());
    public final static Media SUMMON = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/summon.mp3")).toExternalForm());
    public final static Media EFFECT = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/spell.mp3")).toExternalForm());
    public final static Media SWAP_POSITION = new Media(Objects.requireNonNull(RootMenu.class.getResource("/assets/sfx/swap_position.mp3")).toExternalForm());

    public static void playMedia(Media media) {
        MediaPlayer musicPlayer = new MediaPlayer(media);
        musicPlayer.setOnEndOfMedia(musicPlayer::dispose);
        musicPlayer.play();
    }
}
