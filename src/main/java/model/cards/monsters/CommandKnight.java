package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

import java.util.Arrays;
import java.util.Optional;

public class CommandKnight extends EffectMonsters {
    private static CommandKnight instance;
    private final static int ATTACK_DELTA = 400;

    private CommandKnight() {
        super("Command Knight");
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new CommandKnight();
    }

    public static int getAttackDelta() {
        return ATTACK_DELTA;
    }

    @Override
    public void activateEffect() {

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
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard) {
        Optional<PlayableCard> thisCard = Arrays.stream(rivalBoard.getMonsterCards()).filter(x -> x.getCard().getName().equals(getName())).findFirst();
        return thisCard.filter(playableCard -> playableCard.isAttacking() && rivalBoard.countActiveMonsterCards() >= 1).isPresent();
    }
}
