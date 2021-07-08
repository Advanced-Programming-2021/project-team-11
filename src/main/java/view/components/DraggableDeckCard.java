package view.components;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import model.cards.Card;

public class DraggableDeckCard extends ImageView {
    public enum CardPlace {
        INVENTORY,
        MAIN_DECK,
        SIDE_DECK,
    }

    private final CardPlace cardPlace;

    public DraggableDeckCard(Card card, CardPlace cardPlace) {
        setFitHeight(100);
        setFitWidth(70);
        setImage(Assets.getCardImage(card));
        Tooltip.install(this, new Tooltip(card.toString()));
        setupDrag(card);
        this.cardPlace = cardPlace;
    }

    private void setupDrag(Card card) {
        final DraggableDeckCard draggableDeckCard = this;
        setOnDragDetected(event -> {
            Dragboard db = draggableDeckCard.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(draggableDeckCard.snapshot(null, null));
            ClipboardContent content = new ClipboardContent();
            content.putString(card.getName());
            db.setContent(content);
        });
        setOnMouseDragged(event -> event.setDragDetect(true));
        setOnMouseEntered(event -> {
            if (!event.isPrimaryButtonDown()) {
                draggableDeckCard.setCursor(Cursor.MOVE);
            }
        });
        setOnMouseExited(event -> {
            if (!event.isPrimaryButtonDown()) {
                draggableDeckCard.setCursor(Cursor.DEFAULT);
            }
        });
        setOnMouseReleased(event -> draggableDeckCard.setCursor(Cursor.MOVE));
    }

    public CardPlace getCardPlace() {
        return cardPlace;
    }
}
