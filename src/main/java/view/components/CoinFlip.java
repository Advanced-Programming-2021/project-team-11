package view.components;

import controller.GameUtils;
import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.enums.CoinFlipResult;

public class CoinFlip extends Circle {
    public interface CoinFlipCallback {
        void callback(CoinFlipResult result);
    }

    private static final int ANIMATION_DURATION = 3;
    private final CoinFlipCallback callback;

    public CoinFlip(CoinFlipCallback callback) {
        super(50);
        this.callback = callback;
        super.setFill(Assets.getCoinImage(0));
    }

    public void flip() {
        final Circle image = this;
        final CoinFlipResult coinFlipResult = GameUtils.flipCoin();
        final int flipCount = GameUtils.getFlipCoinFlipCount(coinFlipResult);
        Transition flipTransition = new Transition() {
            {
                setCycleDuration(Duration.seconds(ANIMATION_DURATION));
            }

            @Override
            protected void interpolate(double frac) {
                frac *= flipCount;
                image.setScaleX(Math.abs(frac - (int) frac - 0.5) * 2);
                if (frac < 0.5)
                    image.setFill(Assets.getCoinImage(0));
                else
                    image.setFill(Assets.getCoinImage((int) (frac + 0.5)));
            }
        };
        flipTransition.setOnFinished(x -> callback.callback(coinFlipResult));
        flipTransition.play();
    }
}
