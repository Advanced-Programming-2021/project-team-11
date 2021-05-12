package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class ManEaterBug extends InitializableEffectMonsters {
    private static ManEaterBug instance;
    private static final String CARD_NAME = "Man-Eater Bug";

    private ManEaterBug() {
        super(CARD_NAME);
    }

    public static ManEaterBug getInstance() {
        if (instance == null)
            instance = new ManEaterBug();
        return instance;
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    /**
     * Removes specified card from rival deck
     *
     * @param myBoard           No effect here. Pass null
     * @param rivalBoard        The rival board to remove card from it
     * @param thisCard          No effect here
     * @param rivalCard         The card to remove from rival board. There is no problem passing an invalid card (null) here
     * @param activationCounter No effect here. Pass 0
     */
    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        if (rivalCard == null)
            return;
        rivalBoard.sendToGraveyard(rivalCard);
    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
