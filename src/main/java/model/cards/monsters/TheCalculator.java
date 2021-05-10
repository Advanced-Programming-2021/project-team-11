package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class TheCalculator extends EffectMonsters {
    private static TheCalculator instance;
    private final static String CARD_NAME = "The Calculator";
    private final static int DAMAGE_MULTIPLIER = 300;

    private TheCalculator() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new TheCalculator();
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    public static int getAttackMultiplier() {
        return DAMAGE_MULTIPLIER;
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
