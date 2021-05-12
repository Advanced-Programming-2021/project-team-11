package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class Texchanger extends InitializableEffectMonsters {
    private static Texchanger instance;
    private final static String CARD_NAME = "Texchanger";

    private Texchanger() {
        super(CARD_NAME);
    }

    public static Texchanger getInstance() {
        if (instance == null)
            instance = new Texchanger();
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
