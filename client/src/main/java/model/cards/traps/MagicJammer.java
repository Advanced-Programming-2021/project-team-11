package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.TrapCard;
import model.cards.TrapCardType;

public class MagicJammer extends TrapCard {
    private final static String CARD_NAME = "Magic Jammer";
    private static MagicJammer instance;

    private MagicJammer() {
        super(CARD_NAME, TrapCardType.COUNTER);
    }

    public static MagicJammer getInstance() {
        if (instance == null)
            instance = new MagicJammer();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {}

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return rivalBoard.getSpellCardsList().stream().anyMatch(card -> card.getCard() instanceof MagicJammer);
    }
}
