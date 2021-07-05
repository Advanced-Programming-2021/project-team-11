package view.components;

import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import model.PlayableCard;
import model.enums.CardPlaceType;
import view.menus.SceneChanger;

public class CardView extends ImageView {
    private final PlayableCard card;
    private final boolean forRival;

    public CardView(PlayableCard card, boolean forRival, CardHoverCallback hoverCallback) {
        setFitHeight(100);
        setFitWidth(70);
        this.card = card;
        this.forRival = forRival;
        setupListeners(hoverCallback);
        renderCard();
    }

    private void setupListeners(CardHoverCallback hoverCallback) {
        setOnMouseEntered(x -> {
            if (hoverCallback != null)
                hoverCallback.cardHovered(card.getCard(), shouldShowInfo());
            SceneChanger.getScene().setCursor(Cursor.HAND);
        });

        setOnMouseExited(x -> SceneChanger.getScene().setCursor(Cursor.DEFAULT));
    }

    public void renderCard() {
        super.setImage(shouldRenderCardImage() ? Assets.UNKNOWN_CARD : Assets.getCardImage(card.getCard()));
        super.setRotate(getRotation());
    }

    private boolean shouldRenderCardImage() {
        if (card.getCardPlace() == CardPlaceType.HAND)
            return forRival;
        return card.isHidden();
    }

    private double getRotation() {
        if (card.getCardPlace() == CardPlaceType.GRAVEYARD || card.getCardPlace() == CardPlaceType.HAND)
            return 0;
        return card.isAttacking() ? 0 : 90;
    }

    public boolean shouldShowInfo() {
        return !(forRival && card.isHidden());
    }
}
