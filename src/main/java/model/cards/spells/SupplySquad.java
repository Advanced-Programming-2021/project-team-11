package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;

public class SupplySquad extends SpellCard {
    private final static String CARD_NAME = "Supply Squad";
    private static SupplySquad instance;

    private SupplySquad() {
        super(CARD_NAME, SpellCardType.CONTINUES, false);
    }

    public static SupplySquad getInstance() {
        if (instance == null)
            instance = new SupplySquad();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        myBoard.drawCard();
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return activationCounter == 0;
    }

    @Override
    public void throwConditionNotMadeException() {

    }
}
