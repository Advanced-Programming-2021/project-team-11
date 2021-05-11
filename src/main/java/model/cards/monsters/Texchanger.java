package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class Texchanger extends InitializableEffectMonsters {
    private static Texchanger instance;
    private final static String CARD_NAME = "Texchanger";

    private Texchanger() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new Texchanger();
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
