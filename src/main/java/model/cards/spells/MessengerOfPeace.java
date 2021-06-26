package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;

public class MessengerOfPeace extends SpellCard {
    private final static String CARD_NAME = "Messenger of peace";
    private final static int HEALTH_REDUCED = 100, MAX_ATTACK = 1500;
    private static MessengerOfPeace instance;

    private MessengerOfPeace() {
        super(CARD_NAME, SpellCardType.CONTINUES, false);
    }

    public static MessengerOfPeace getInstance() {
        if (instance == null)
            instance = new MessengerOfPeace();
        return instance;
    }

    public static int getHealthReduced() {
        return HEALTH_REDUCED;
    }

    public static int getMaxAttack() {
        return MAX_ATTACK;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        myBoard.getPlayer().increaseHealth(getHealthReduced());
    }

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return true;
    }

    @Override
    public void throwConditionNotMadeException() {}
}
