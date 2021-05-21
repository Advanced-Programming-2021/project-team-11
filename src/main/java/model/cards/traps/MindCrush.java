package model.cards.traps;

import controller.GameUtils;
import model.PlayableCard;
import model.PlayerBoard;
import model.cards.Card;
import model.cards.TrapCard;
import model.cards.TrapCardType;

public class MindCrush extends TrapCard {
    private final static String CARD_NAME = "Mind Crush";
    private static MindCrush instance;

    private MindCrush() {
        super(CARD_NAME, TrapCardType.NORMAL);
    }

    public static MindCrush getInstance() {
        if (instance == null)
            instance = new MindCrush();
        return instance;
    }

    /**
     * @param myBoard           Player's board who owns this card
     * @param rivalBoard        Rivals board
     * @param thisCard          Not used
     * @param rivalCard         The card which player has chosen
     * @param activationCounter No effect
     */
    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        boolean correct = rivalBoard.getHand().stream().anyMatch(playableCard -> playableCard.getCard().getName().equals(rivalCard.getCard().getName()));
        if (correct) {
            rivalBoard.getHand().removeIf(card -> card.getCard().getName().equals(rivalCard.getCard().getName()));
            rivalBoard.getGraveyard().removeIf(card -> card.getCard().getName().equals(rivalCard.getCard().getName()));
            rivalBoard.getDeck().removeIf(card -> card.getName().equals(rivalCard.getCard().getName()));
        } else {
            if (myBoard.getHand().size() != 0)
                myBoard.getHand().remove(GameUtils.random.nextInt(myBoard.getHand().size()));
        }
    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
