package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class ManEaterBug extends EffectMonsters {
    private static ManEaterBug instance;
    private static final String CARD_NAME = "Man-Eater Bug";

    private ManEaterBug() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new ManEaterBug();
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    /**
     * Removes specified card from rival deck
     *
     * @param myBoard           No effect here. Pass null
     * @param rivalBoard        The rival board to remove card from it
     * @param card              The card to remove from rival board. There is no problem passing an invalid card here
     * @param activationCounter No effect here. Pass 0
     */
    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard card, int activationCounter) {
        if (card == null)
            return;
        rivalBoard.moveMonsterToGraveyard(card);
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard) {
        return false;
    }
}
