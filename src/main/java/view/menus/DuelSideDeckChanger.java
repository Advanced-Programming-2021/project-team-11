package view.menus;

import controller.menucontrollers.DeckMenuController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Deck;
import model.User;
import model.exceptions.*;
import view.components.AlertsUtil;
import view.components.DeckViewUtil;
import view.components.DraggableDeckCard;

import java.net.URL;
import java.util.ResourceBundle;

public class DuelSideDeckChanger implements Initializable {
    private static User[] users;
    private static int counter;
    private Deck deck;
    private int initialMainDeckSize;
    public Text deckName;
    public ScrollPane mainDeckScrollPane;
    public AnchorPane mainDeckAnchorPane;
    public ScrollPane sideDeckScrollPane;
    public AnchorPane sideDeckAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deck = users[counter].getActiveDeck();
        deckName.setText(users[counter].getActiveDeckName());
        initialMainDeckSize = deck.getMainDeck().size();
        generateCards();
    }

    private void generateCards() {
        DeckViewUtil.addCards(deck.getMainDeck(), mainDeckAnchorPane, mainDeckScrollPane, 8, DraggableDeckCard.CardPlace.MAIN_DECK, this::handleMove);
        DeckViewUtil.addCards(deck.getSideDeck(), sideDeckAnchorPane, sideDeckScrollPane, 8, DraggableDeckCard.CardPlace.SIDE_DECK, this::handleMove);
    }

    private void handleMove(String cardName, DraggableDeckCard.CardPlace initialPlace, DraggableDeckCard.CardPlace destinationPlace) {
        try {
            if (initialPlace == DraggableDeckCard.CardPlace.INVENTORY)
                DeckMenuController.addCardToDeck(users[counter], deckName.getText(), cardName, destinationPlace == DraggableDeckCard.CardPlace.SIDE_DECK);
            else {
                if (destinationPlace == DraggableDeckCard.CardPlace.INVENTORY)
                    DeckMenuController.removeCardFromDeck(users[counter], deckName.getText(), cardName, initialPlace == DraggableDeckCard.CardPlace.SIDE_DECK);
                else
                    DeckMenuController.swapCardBetweenDecks(users[counter], deckName.getText(), cardName, initialPlace == DraggableDeckCard.CardPlace.SIDE_DECK);
            }
            Platform.runLater(this::generateCards);
        } catch (DeckDoesNotExistsException | DeckCardNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            Platform.runLater(() -> AlertsUtil.showError(e));
        }
    }

    public void clickedContinueButton(MouseEvent mouseEvent) {
        // Check deck size
        if (deck.getMainDeck().size() != initialMainDeckSize) {
            AlertsUtil.showError("Your initialize main deck size is not the same as current main deck size.\nThe initialize size was " + initialMainDeckSize + " but the current size is " + deck.getMainDeck().size());
            return;
        }
        // Check for second player or duel
        mainDeckAnchorPane.getChildren().clear();
        sideDeckAnchorPane.getChildren().clear();
        if (counter == 0) {
            counter++;
            AlertsUtil.showHelp("Pass the computer to " + users[1].getNickname());
            SceneChanger.changeScene(MenuNames.DUEL_SIDE_DECK_CHANGER);
            return;
        }
        DuelMenu.gameController.setupNewRound();
        SceneChanger.changeScene(MenuNames.DUEL);
    }

    public static void setUsers(User user1, User user2) {
        counter = 0;
        users = new User[]{user1, user2};
    }
}
