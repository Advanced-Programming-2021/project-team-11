package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class ExploderDragon extends InitializableEffectMonsters {
    private static ExploderDragon instance;
    private final static String CARD_NAME = "Exploder Dragon";

    private ExploderDragon() {
        super(CARD_NAME);
    }

    public static ExploderDragon getInstance() {
        if (instance == null)
            instance = new ExploderDragon();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        rivalBoard.sendToGraveyard(rivalCard);
    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
