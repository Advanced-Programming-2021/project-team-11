package view.components;

import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import model.PlayableCard;
import model.cards.MonsterCard;
import model.enums.CardPlaceType;
import view.menus.SceneChanger;

public class CardView extends ImageView {
    /**
     * Resize the pictures with this multiplier
     */
    private final static double SIZE_MULTIPLIER = 0.15;
    private final PlayableCard card;
    private final boolean forRival;
    /**
     * Index of this card in the board
     */
    private final int index;

    public CardView(PlayableCard card, boolean forRival, CardHoverCallback hoverCallback, int index) {
        setFitHeight(614 * SIZE_MULTIPLIER);
        setFitWidth(421 * SIZE_MULTIPLIER);
        this.card = card;
        this.forRival = forRival;
        this.index = index;
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
        if (card.getCard() instanceof MonsterCard)
            return card.isAttacking() ? 0 : 90;
        return 0;
    }

    public boolean shouldShowInfo() {
        return !(forRival && card.isHidden());
    }

    public int getIndex() {
        return index;
    }
}
