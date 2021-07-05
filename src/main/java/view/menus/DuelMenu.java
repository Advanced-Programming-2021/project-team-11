package view.menus;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import controller.GameController;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.PlayableCard;
import model.User;
import model.cards.Card;
import model.enums.CardPlaceType;
import view.components.CardInfo;
import view.components.CardView;
import view.components.DuelistInfo;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DuelMenu implements Initializable {
    private static final double CARD_PLACES_WIDTH = 76.5, CARD_PLACES_FIRST_X = 800 + 105 - 570,
            CARD_PLACES_SPELL_RIVAL_Y = 85, CARD_PLACES_SPELL_PLAYER_Y = 600 - CARD_PLACES_SPELL_RIVAL_Y - 100,
            CARD_PLACES_MONSTER_RIVAL_Y = 85 + 105, CARD_PLACES_MONSTER_PLAYER_Y = 600 - CARD_PLACES_MONSTER_RIVAL_Y - 100;
    private static final double[] CARD_X_OFFSETS = {CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH * 2, CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH, CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH * 3, CARD_PLACES_FIRST_X, CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH * 4};
    public static GameController gameController;
    public static User player1, player2;
    private static ArrayList<CardView> cardsOnGround = new ArrayList<>();
    public ImageView fieldImageView;
    public DuelistInfo rivalInfo;
    public DuelistInfo playerInfo;
    public CardInfo cardInfo;
    public AnchorPane rootView;
    public JFXDialog dialog;
    public Label dialogHeader;
    public VBox dialogContainer;
    public JFXDialogLayout dialogContainerLayout;
    public StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardsOnGround.clear();
        dialog.setDialogContainer(stackPane);
        updatePlayersLp();
        buildScene();
    }

    /**
     * Updates badges of the players
     */
    private void updatePlayersLp() {
        playerInfo.setUser(gameController.getRound().getPlayerBoard().getPlayer());
        rivalInfo.setUser(gameController.getRound().getRivalBoard().getPlayer());
    }

    private void buildScene() {
        cardsOnGround.forEach(card -> rootView.getChildren().remove(card));
        cardsOnGround.clear();
        addHands();
        addSpells();
        addMonsters();
    }

    /**
     * Renders all cards in both player's hands
     */
    private void addHands() {
        ArrayList<PlayableCard> cards = gameController.getRound().getPlayerBoard().getHand();
        for (int i = 0; i < cards.size(); i++) {
            CardView view = new CardView(cards.get(i), false, this::cardHovered);
            view.setLayoutY(530);
            view.setLayoutX(260 + i * 70);
            rootView.getChildren().add(view);
            cardsOnGround.add(view);
        }
        cards = gameController.getRound().getRivalBoard().getHand();
        for (int i = 0; i < cards.size(); i++) {
            CardView view = new CardView(cards.get(i), true, this::cardHovered);
            view.setLayoutY(-40);
            view.setLayoutX(260 + i * 70);
            rootView.getChildren().add(view);
            cardsOnGround.add(view);
        }
    }

    /**
     * Renders all spells in the field
     */
    private void addSpells() {
        addMainCards(gameController.getRound().getRivalBoard().getSpellCards(), true, CARD_PLACES_SPELL_RIVAL_Y);
        addMainCards(gameController.getRound().getPlayerBoard().getSpellCards(), false, CARD_PLACES_SPELL_PLAYER_Y);
    }

    private void addMonsters() {
        addMainCards(gameController.getRound().getRivalBoard().getMonsterCards(), true, CARD_PLACES_MONSTER_RIVAL_Y);
        addMainCards(gameController.getRound().getPlayerBoard().getMonsterCards(), false, CARD_PLACES_MONSTER_PLAYER_Y);
    }

    private void addMainCards(PlayableCard[] cards, boolean forRival, double y) {
        for (int i = 0; i < cards.length; i++) {
            PlayableCard card = cards[i];
            if (card != null) {
                CardView view = new CardView(card, forRival, this::cardHovered);
                view.setLayoutY(y);
                view.setLayoutX(CARD_X_OFFSETS[i]);
                rootView.getChildren().add(view);
                cardsOnGround.add(view);
            }
        }
    }

    private void cardHovered(Card card, boolean shouldShow) {
        cardInfo.setCard(shouldShow ? card : null);
    }
}
