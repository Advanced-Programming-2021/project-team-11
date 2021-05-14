package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;

public class DarkHole extends SpellCard {
    private final static String CARD_NAME = "Dark Hole";
    private static DarkHole instance;

    private DarkHole() {
        super(CARD_NAME, SpellCardType.NORMAL, false);
    }

    public static DarkHole getInstance() {
        if (instance == null)
            instance = new DarkHole();
        return instance;
    }

    /**
     * Sends all rival monsters to graveyard
     *
     * @param myBoard           The board to remove this card from
     * @param rivalBoard        Removes all monsters from this board
     * @param thisCard          Removes this card from myBoard
     * @param rivalCard         No effect
     * @param activationCounter No effect
     */
    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        for (PlayableCard card : rivalBoard.getSpellCards())
            if (card != null)
                rivalBoard.sendToGraveyard(card);
        for (PlayableCard card : rivalBoard.getMonsterCards())
            if (card != null)
                rivalBoard.sendToGraveyard(card);
        myBoard.sendToGraveyard(thisCard);
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return true;
    }

    @Override
    public void throwConditionNotMadeException() {

    }
}
