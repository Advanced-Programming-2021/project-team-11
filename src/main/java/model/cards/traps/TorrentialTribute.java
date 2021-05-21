package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.TrapCard;
import model.cards.TrapCardType;

import java.util.Arrays;
import java.util.Objects;

public class TorrentialTribute extends TrapCard {
    private final static String CARD_NAME = "Torrential Tribute";
    private static TorrentialTribute instance;

    private TorrentialTribute() {
        super(CARD_NAME, TrapCardType.NORMAL);
    }

    public static TorrentialTribute getInstance() {
        if (instance == null)
            instance = new TorrentialTribute();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        Arrays.stream(myBoard.getMonsterCards()).filter(Objects::nonNull).forEach(myBoard::sendToGraveyard);
        Arrays.stream(rivalBoard.getMonsterCards()).filter(Objects::nonNull).forEach(rivalBoard::sendToGraveyard);
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return rivalBoard.getSpellCardsList().stream().anyMatch(card -> card.getCard() instanceof TorrentialTribute);
    }
}
