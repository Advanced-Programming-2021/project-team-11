package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.TrapCard;
import model.cards.TrapCardType;

public class CallOfTheHaunted extends TrapCard {
    private final static String CARD_NAME = "Call of the Haunted";
    private static CallOfTheHaunted instance;

    private CallOfTheHaunted() {
        super(CARD_NAME, TrapCardType.CONTINUES);
    }

    public static CallOfTheHaunted getInstance() {
        if (instance == null)
            instance = new CallOfTheHaunted();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !myBoard.getGraveyard().isEmpty() && !myBoard.isMonsterZoneFull();
    }
}
