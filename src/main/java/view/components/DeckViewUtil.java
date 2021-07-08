package view.components;

import com.jfoenix.controls.JFXScrollPane;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import model.cards.Card;

import java.util.ArrayList;

public class DeckViewUtil {
    public interface MoveHandler {
        void handleMove(String cardName, DraggableDeckCard.CardPlace initialPlace, DraggableDeckCard.CardPlace destinationPlace);
    }

    public static void addCards(ArrayList<Card> cards, AnchorPane anchorPane, ScrollPane scrollPane, int inEachRow, DraggableDeckCard.CardPlace cardPlace, MoveHandler moveHandler) {
        anchorPane.getChildren().clear();
        for (int i = 0; i < cards.size(); i++) {
            DraggableDeckCard cardImageView = new DraggableDeckCard(cards.get(i), cardPlace);
            cardImageView.setX(85 * (i % inEachRow) + 10);
            int c = i / inEachRow;
            cardImageView.setY(115 * c + 10);
            anchorPane.getChildren().add(cardImageView);
        }
        setDragAcceptable(scrollPane, cardPlace, moveHandler);
        JFXScrollPane.smoothScrolling(scrollPane);
    }

    private static void setDragAcceptable(Node node, final DraggableDeckCard.CardPlace place, MoveHandler moveHandler) {
        node.setOnDragOver(event -> {
            if (checkDragSource(event.getGestureSource(), place) && event.getDragboard().hasString())
                event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });

        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                moveHandler.handleMove(db.getString(), ((DraggableDeckCard) event.getGestureSource()).getCardPlace(), place);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private static boolean checkDragSource(Object object, DraggableDeckCard.CardPlace cardPlace) {
        return object instanceof DraggableDeckCard && ((DraggableDeckCard) object).getCardPlace() != cardPlace;
    }
}
