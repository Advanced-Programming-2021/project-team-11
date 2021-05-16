package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;

public class ChangeOfHeart extends SpellCard {
    private final static String CARD_NAME = "Change of Heart";
    private static ChangeOfHeart instance;

    private ChangeOfHeart() {
        super(CARD_NAME, SpellCardType.NORMAL, true);
    }

    public static ChangeOfHeart getInstance() {
        if (instance == null)
            instance = new ChangeOfHeart();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        // Must be handled from controller
    }

    @Override
    public void deactivateEffect() {
        // Must be handled from controller
    }

    /**
     * Checks if rival monster zone is not empty and player board is not full
     *
     * @param myBoard           Player board
     * @param rivalBoard        Rival board
     * @param thisCard          no effect
     * @param activationCounter no effect
     * @return True if condition is made
     */
    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !myBoard.isMonsterZoneFull() && !rivalBoard.isMonsterZoneEmpty();
    }

    @Override
    public void throwConditionNotMadeException() {

    }
}
