package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class Suijin extends InitializableEffectMonsters {
    private final static String CARD_NAME = "Suijin";
    private static Suijin instance;

    private Suijin() {
        super(CARD_NAME);
    }

    public static Suijin getInstance() {
        if (instance == null)
            instance = new Suijin();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return activationCounter < 1;
    }
}
