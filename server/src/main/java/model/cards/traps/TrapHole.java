package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterCard;
import model.cards.TrapCard;
import model.cards.TrapCardType;

public class TrapHole extends TrapCard {
    private final static String CARD_NAME = "Trap Hole";
    private final static int MIN_ATTACK = 1000;
    private static TrapHole instance;

    private TrapHole() {
        super(CARD_NAME, TrapCardType.NORMAL);
    }

    public static TrapHole getInstance() {
        if (instance == null)
            instance = new TrapHole();
        return instance;
    }

    public static int getMinAttack() {
        return MIN_ATTACK;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        rivalBoard.getHand().remove(rivalCard);
    }

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return rivalBoard.getSpellCardsList().stream().anyMatch(card -> card.getCard() instanceof TrapHole) && ((MonsterCard) thisCard.getCard()).getAttack() >= getMinAttack();
    }
}
