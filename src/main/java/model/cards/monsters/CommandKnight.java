package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

import java.util.Optional;

public class CommandKnight extends EffectMonsters {
    private static CommandKnight instance;
    private final static String CARD_NAME = "Command Knight";
    private final static int ATTACK_DELTA = 400;

    private CommandKnight() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new CommandKnight();
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    public static int getAttackDelta() {
        return ATTACK_DELTA;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    /**
     * The condition determines if this player's card can be attacked or not
     *
     * @param myBoard    Has no effect
     * @param rivalBoard The board which has this card
     * @return True if rival board only have more than one card (another card other than Command Knight)
     */
    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return thisCard.isAttacking()
                && rivalBoard.countActiveMonsterCards() - rivalBoard.getMonsterCardsList().stream().filter(x -> x.getCard().getName().equals(getName())).count() >= 1;
    }
}
