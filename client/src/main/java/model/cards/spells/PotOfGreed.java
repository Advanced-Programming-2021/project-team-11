package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;
import model.exceptions.CantUseSpellException;

public class PotOfGreed extends SpellCard {
    private final static String CARD_NAME = "Pot of Greed";
    private static PotOfGreed instance;

    private PotOfGreed() {
        super(CARD_NAME, SpellCardType.NORMAL, false);
    }

    public static PotOfGreed getInstance() {
        if (instance == null)
            instance = new PotOfGreed();
        return instance;
    }

    /**
     * Simply draws two cards from deck
     *
     * @param myBoard           The board to draw from
     * @param rivalBoard        No effect
     * @param thisCard          Removes this card from myBoard
     * @param rivalCard         No effect
     * @param activationCounter No effect
     */
    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        myBoard.drawCard();
        myBoard.drawCard();
        myBoard.sendToGraveyard(thisCard);
    }

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return myBoard.getHand().size() <= 4;
    }

    @Override
    public void throwConditionNotMadeException() throws CantUseSpellException {
        throw new CantUseSpellException("Your hand is full!");
    }
}
