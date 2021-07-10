package view.animation;

import javafx.animation.Transition;
import javafx.scene.Node;

public class CardDestroyedTransition extends Transition {

    private final Node node;

    public CardDestroyedTransition(Node node) {
        this.node = node;
        super.setCycleDuration(Shared.ANIMATION_DURATION);
    }

    @Override
    protected void interpolate(double frac) {
        node.setRotate(frac * 360 * 2);
        node.setScaleX(1 - frac);
        node.setScaleY(1 - frac);
    }
}
