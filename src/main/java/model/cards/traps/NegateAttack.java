package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.TrapCard;
import model.cards.TrapCardType;

public class NegateAttack extends TrapCard {
    private final static String CARD_NAME = "Negate Attack";
    private static NegateAttack instance;

    private NegateAttack() {
        super(CARD_NAME, TrapCardType.COUNTER);
    }

    public static NegateAttack getInstance() {
        if (instance == null)
            instance = new NegateAttack();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        myBoard.removeSpellTrapCard(this);
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return rivalBoard.getSpellCardsList().stream().anyMatch(card -> card.getCard() instanceof NegateAttack);
    }
}
