package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterCard;
import model.cards.TrapCard;
import model.cards.TrapCardType;

public class MagicCylinder extends TrapCard {
    private final static String CARD_NAME = "Magic Cylinder";
    private static MagicCylinder instance;

    private MagicCylinder() {
        super(CARD_NAME, TrapCardType.NORMAL);
    }

    public static MagicCylinder getInstance() {
        if (instance == null)
            instance = new MagicCylinder();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        rivalBoard.getPlayer().decreaseHealth(((MonsterCard) rivalCard.getCard()).getAttack());
        myBoard.removeSpellTrapCard(this);
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return rivalBoard.getSpellCardsList().stream().anyMatch(card -> card.getCard() instanceof MagicCylinder);
    }
}
