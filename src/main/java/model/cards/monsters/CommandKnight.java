package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class CommandKnight extends InitializableEffectMonsters {
    private static CommandKnight instance;
    private final static String CARD_NAME = "Command Knight";
    private final static int ATTACK_DELTA = 400;

    private CommandKnight() {
        super(CARD_NAME);
    }

    public static CommandKnight getInstance() {
        if (instance == null)
            instance = new CommandKnight();
        return instance;
    }

    /**
     * The damage to add to all monsters
     *
     * @return The damage to add to all monsters
     */
    public static int getAttackDelta() {
        return ATTACK_DELTA;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

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
