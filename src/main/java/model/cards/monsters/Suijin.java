package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class Suijin extends InitializableEffectMonsters {
    private static Suijin instance;
    private final static String CARD_NAME = "Suijin";

    private Suijin() {
        super(CARD_NAME);
    }

    public static Suijin getInstance() {
        if (instance == null)
            instance = new Suijin();
        return instance;
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return activationCounter < 1;
    }
}
