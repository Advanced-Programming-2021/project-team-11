package view.menus;

import controller.menucontrollers.DeckMenuController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Deck;
import model.exceptions.*;
import view.components.AlertsUtil;
import view.components.DeckViewUtil;
import view.components.DraggableDeckCard;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckDetailsMenu implements Initializable {
    public static Deck toEditDeck = null;
    public static String toEditDeckName;
    public AnchorPane mainDeckAnchorPane;
    public AnchorPane inventoryAnchorPane;
    public AnchorPane sideDeckAnchorPane;
    public ScrollPane mainDeckScrollPane;
    public ScrollPane inventoryScrollPane;
    public ScrollPane sideDeckScrollPane;
    public Text deckName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateCards();
        deckName.setText(toEditDeckName);
    }

    private void generateCards() {
        DeckViewUtil.addCards(toEditDeck.getMainDeck(), mainDeckAnchorPane, mainDeckScrollPane, 5, DraggableDeckCard.CardPlace.MAIN_DECK, this::handleMove);
        DeckViewUtil.addCards(toEditDeck.getSideDeck(), sideDeckAnchorPane, sideDeckScrollPane, 5, DraggableDeckCard.CardPlace.SIDE_DECK, this::handleMove);
        DeckViewUtil.addCards(MainMenu.loggedInUser.getAvailableCards(), inventoryAnchorPane, inventoryScrollPane, 3, DraggableDeckCard.CardPlace.INVENTORY, this::handleMove);
    }

    private void handleMove(String cardName, DraggableDeckCard.CardPlace initialPlace, DraggableDeckCard.CardPlace destinationPlace) {
        try {
            if (initialPlace == DraggableDeckCard.CardPlace.INVENTORY)
                DeckMenuController.addCardToDeck(MainMenu.loggedInUser, deckName.getText(), cardName, destinationPlace == DraggableDeckCard.CardPlace.SIDE_DECK);
            else {
                if (destinationPlace == DraggableDeckCard.CardPlace.INVENTORY)
                    DeckMenuController.removeCardFromDeck(MainMenu.loggedInUser, deckName.getText(), cardName, initialPlace == DraggableDeckCard.CardPlace.SIDE_DECK);
                else
                    DeckMenuController.swapCardBetweenDecks(MainMenu.loggedInUser, deckName.getText(), cardName, initialPlace == DraggableDeckCard.CardPlace.SIDE_DECK);
            }
            Platform.runLater(this::generateCards);
        } catch (DeckDoesNotExistsException | DeckCardNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            Platform.runLater(() -> AlertsUtil.showError(e));
        }

    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.DECK);
    }
}
