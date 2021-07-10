package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;
import model.exceptions.CantUseSpellException;

public class Terraforming extends SpellCard {
    private final static String CARD_NAME = "Terraforming";
    private static Terraforming instance;

    private Terraforming() {
        super(CARD_NAME, SpellCardType.NORMAL, true);
    }

    public static Terraforming getInstance() {
        if (instance == null)
            instance = new Terraforming();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {}

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return myBoard.getHand().size() != 6 && myBoard.getDeck().stream().anyMatch(card -> card instanceof SpellCard && ((SpellCard) card).getSpellCardType() == SpellCardType.FIELD);
    }

    @Override
    public void throwConditionNotMadeException() throws CantUseSpellException {
        throw new CantUseSpellException("you hand is full or you don't have a field card in your deck!");
    }
}
