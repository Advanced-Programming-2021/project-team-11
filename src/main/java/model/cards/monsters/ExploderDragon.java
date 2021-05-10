package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class ExploderDragon extends EffectMonsters {
    private static ExploderDragon instance;
    private final static String CARD_NAME = "Exploder Dragon";

    private ExploderDragon() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new ExploderDragon();
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        rivalBoard.sendToGraveyard(rivalCard);
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
