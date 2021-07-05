package view.menus;

import controller.GameController;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.PlayableCard;
import model.User;
import model.cards.Card;
import view.components.CardInfo;
import view.components.CardView;
import view.components.DuelistInfo;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DuelMenu implements Initializable {
    public static GameController gameController;
    public static User player1, player2;
    private static ArrayList<CardView> cardsOnGround = new ArrayList<>();
    public ImageView fieldImageView;
    public DuelistInfo rivalInfo;
    public DuelistInfo playerInfo;
    public CardInfo cardInfo;
    public AnchorPane rootView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardsOnGround.clear();
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
        addHands();
    }

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

    private void cardHovered(Card card, boolean shouldShow) {
        cardInfo.setCard(shouldShow ? card : null);
    }
}
