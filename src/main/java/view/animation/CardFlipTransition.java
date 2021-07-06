package view.animation;

import javafx.animation.Transition;
import view.components.CardView;

public class CardFlipTransition extends Transition {
    private final CardView cardView;
    private boolean hasRendered = false;

    public CardFlipTransition(CardView cardView) {
        this.cardView = cardView;
        super.setCycleDuration(Shared.ANIMATION_DURATION);
    }

    @Override
    protected void interpolate(double frac) {
        cardView.setScaleX(Math.abs(frac - 0.5) * 2);
        if (!hasRendered && frac >= 0.5) {
            cardView.renderCard();
            hasRendered = true;
        }
    }
}
