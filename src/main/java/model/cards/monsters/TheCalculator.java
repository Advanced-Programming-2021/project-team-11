package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class TheCalculator extends InitializableEffectMonsters {
    private final static String CARD_NAME = "The Calculator";
    private final static int DAMAGE_MULTIPLIER = 300;
    private static TheCalculator instance;

    private TheCalculator() {
        super(CARD_NAME);
    }

    public static TheCalculator getInstance() {
        if (instance == null)
            instance = new TheCalculator();
        return instance;
    }

    public static int getAttackMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
