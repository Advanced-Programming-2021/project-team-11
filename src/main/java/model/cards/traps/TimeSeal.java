package model.cards.traps;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.TrapCard;
import model.cards.TrapCardType;
import model.exceptions.PlayerTimeSealedException;

import java.util.Optional;

public class TimeSeal extends TrapCard {
    private final static String CARD_NAME = "Time Seal";
    private static TimeSeal instance;

    private TimeSeal() {
        super(CARD_NAME, TrapCardType.NORMAL);
    }

    public static TimeSeal getInstance() {
        if (instance == null)
            instance = new TimeSeal();
        return instance;
    }

    /**
     * Checks if the player is sealed and cannot pickup card
     * Please note that this card is dismissed if player was sealed
     *
     * @param rivalBoard The rival board to check it
     * @throws PlayerTimeSealedException If the player was sealed
     */
    public void isPlayerSealed(PlayerBoard rivalBoard) throws PlayerTimeSealedException {
        Optional<PlayableCard> timeSealCard = rivalBoard.getSpellCardsList().stream().filter(card -> card.getCard() instanceof TimeSeal && !card.isHidden()).findFirst();
        if (timeSealCard.isPresent()) {
            rivalBoard.sendToGraveyard(timeSealCard.get());
            throw new PlayerTimeSealedException();
        }
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return true;
    }
}
