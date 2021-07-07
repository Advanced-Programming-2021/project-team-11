package view.menus;

import com.jfoenix.controls.JFXButton;
import controller.GameRoundController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.PlayableCard;
import model.cards.monsters.BeastKingBarbaros;
import model.exceptions.BooAnException;
import model.exceptions.NoMonsterOnTheseAddressesException;
import model.exceptions.NotEnoughCardsToTributeException;
import model.exceptions.TrapCanBeActivatedException;
import view.components.AlertsUtil;
import view.components.Assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

public class CardSpecificMenus {
    public static ArrayList<Integer> readCardsToTribute(PlayableCard[] cards, int cardsNeeded) {
        if (Arrays.stream(cards).filter(Objects::nonNull).count() < cardsNeeded)
            throw new BooAnException("cards.size() < cardsNeeded: " + Arrays.toString(cards) + " " + cardsNeeded);
        if (cardsNeeded == 0)
            return new ArrayList<>();
        // Create a new stage for this
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        borderPane.setTop(new Label("Select cards to tribute: (" + cardsNeeded + " cards needed)"));
        HBox cardBox = new HBox(5);
        TreeSet<Integer> toTributeCards = new TreeSet<>();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == null)
                continue;
            final int index = i + 1;
            ImageView deathIcon = new ImageView(Assets.DEATH);
            deathIcon.setVisible(false);
            ImageView card = new ImageView(Assets.getCardImage(cards[i].getCard()));
            card.setFitHeight(100);
            card.setFitWidth(70);
            EventHandler<? super MouseEvent> clicked = x -> {
                if (toTributeCards.contains(index)) {
                    deathIcon.setVisible(false);
                    toTributeCards.remove(index);
                } else {
                    deathIcon.setVisible(true);
                    toTributeCards.add(index);
                }
            };
            card.setOnMouseClicked(clicked);
            deathIcon.setOnMouseClicked(clicked);
            cardBox.getChildren().add(new StackPane(card, deathIcon));
        }
        borderPane.setCenter(cardBox);
        HBox buttons = new HBox(5);
        buttons.setPadding(new Insets(5, 0, 0, 0));
        buttons.setAlignment(Pos.CENTER);
        JFXButton tributeButton = new JFXButton("Tribute");
        tributeButton.setOnMouseClicked(x -> {
            if (toTributeCards.size() != cardsNeeded)
                AlertsUtil.showError("Invalid amount of cards chosen!");
            else
                stage.close();
        });
        JFXButton cancelButton = new JFXButton("Cancel");
        cancelButton.setOnMouseClicked(x -> {
            toTributeCards.clear();
            stage.close();
        });
        buttons.getChildren().addAll(tributeButton, cancelButton);
        borderPane.setBottom(buttons);
        stage.showAndWait();
        if (toTributeCards.size() != cardsNeeded)
            return null;
        return new ArrayList<>(toTributeCards);
    }

    public static void summonBeastKingBarbaros(GameRoundController gameRoundController) {
        int tributes, monstersHave = gameRoundController.getPlayerBoard().countActiveMonsterCards();
        while (true) {
            try {
                int result = AlertsUtil.customButtonAlert("", "How many cards you want to tribute?", "0", "2", "3");
                if (result == -1)
                    return;
                if (result > 0)
                    result++;
                tributes = result;
                if (tributes > monstersHave)
                    throw new NotEnoughCardsToTributeException(null);
                break;
            } catch (NotEnoughCardsToTributeException ex) {
                AlertsUtil.showError(ex);
            }
        }
        Stage stage = new Stage();
        stage.showAndWait();
        ArrayList<Integer> cards = readCardsToTribute(gameRoundController.getPlayerBoard().getMonsterCards(), tributes);
        if (cards == null)
            return;
        if (tributes == 0)
            gameRoundController.returnSelectedCard().addAttackDelta(BeastKingBarbaros.getToReduceAttack());
        try {
            gameRoundController.summonCard(cards, true);
        } catch (NoMonsterOnTheseAddressesException | TrapCanBeActivatedException e) {
            AlertsUtil.showError(e);
            return;
        }
        if (tributes == 3)
            Arrays.stream(gameRoundController.getRivalBoard().getMonsterCards()).forEach(card -> gameRoundController.getRivalBoard().sendToGraveyard(card));
    }
}
