package view.menus;

import com.jfoenix.controls.JFXScrollPane;
import controller.menucontrollers.DeckMenuController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Deck;
import model.cards.Card;
import model.exceptions.*;
import view.components.AlertsUtil;
import view.components.DraggableDeckCard;

import java.net.URL;
import java.util.ArrayList;
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
        addCards(toEditDeck.getMainDeck(), mainDeckAnchorPane, mainDeckScrollPane, 5, DraggableDeckCard.CardPlace.MAIN_DECK);
        addCards(toEditDeck.getSideDeck(), sideDeckAnchorPane, sideDeckScrollPane, 5, DraggableDeckCard.CardPlace.SIDE_DECK);
        addCards(MainMenu.loggedInUser.getAvailableCards(), inventoryAnchorPane, inventoryScrollPane, 3, DraggableDeckCard.CardPlace.INVENTORY);
    }

    private void addCards(ArrayList<Card> cards, AnchorPane anchorPane, ScrollPane scrollPane, int inEachRow, DraggableDeckCard.CardPlace cardPlace) {
        anchorPane.getChildren().clear();
        for (int i = 0; i < cards.size(); i++) {
            DraggableDeckCard cardImageView = new DraggableDeckCard(cards.get(i), cardPlace);
            cardImageView.setX(85 * (i % inEachRow) + 10);
            int c = i / inEachRow;
            cardImageView.setY(115 * c + 10);
            anchorPane.getChildren().add(cardImageView);
        }
        setDragAcceptable(scrollPane, cardPlace);
        JFXScrollPane.smoothScrolling(scrollPane);
    }

    private void setDragAcceptable(Node node, final DraggableDeckCard.CardPlace place) {
        node.setOnDragOver(event -> {
            if (checkDragSource(event.getGestureSource(), place) && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                handleMove(db.getString(), ((DraggableDeckCard) event.getGestureSource()).getCardPlace(), place);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
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

    private static boolean checkDragSource(Object object, DraggableDeckCard.CardPlace cardPlace) {
        return object instanceof DraggableDeckCard && ((DraggableDeckCard) object).getCardPlace() != cardPlace;
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.DECK);
    }
}
