package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class TheTricky extends EffectMonsters {
    private static TheTricky instance;
    private final static String CARD_NAME = "The Tricky";

    private TheTricky() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new TheTricky();
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
