package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.TrapCard;
import model.cards.TrapCardType;

public class MirrorForce extends TrapCard {
    private final static String CARD_NAME = "Mirror Force";
    private static MirrorForce instance;

    private MirrorForce() {
        super(CARD_NAME, TrapCardType.NORMAL);
    }

    public static MirrorForce getInstance() {
        if (instance == null)
            instance = new MirrorForce();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        for (int i = 0; i < rivalBoard.getMonsterCards().length; i++)
            if (rivalBoard.getMonsterCards()[i] != null && rivalBoard.getMonsterCards()[i].isAttacking())
                rivalBoard.sendMonsterToGraveyard(i);
        // Remove this card from user board
        myBoard.removeSpellTrapCard(this);
    }

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return rivalBoard.getSpellCardsList().stream().anyMatch(card -> card.getCard() instanceof MirrorForce);
    }
}
