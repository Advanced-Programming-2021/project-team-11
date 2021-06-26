package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;
import model.exceptions.CantUseSpellException;

public class TwinTwisters extends SpellCard {
    private final static String CARD_NAME = "Twin Twisters";
    private static TwinTwisters instance;

    private TwinTwisters() {
        super(CARD_NAME, SpellCardType.QUICK_PLAY, true);
    }

    public static TwinTwisters getInstance() {
        if (instance == null)
            instance = new TwinTwisters();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {}

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return myBoard.getHand().size() != 0;
    }

    @Override
    public void throwConditionNotMadeException() throws CantUseSpellException {
        throw new CantUseSpellException("You don't have any cards in your hand!");
    }
}
