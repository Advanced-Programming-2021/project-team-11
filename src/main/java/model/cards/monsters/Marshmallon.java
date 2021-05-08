package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class Marshmallon extends EffectMonsters {
    private static Marshmallon instance;
    private final static String CARD_NAME = "Marshmallon";
    private final static int TO_REDUCE_HP = 1000;

    private Marshmallon() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new Marshmallon();
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    /**
     * When this card is faced down (hidden) and it's attacked to, we must reduce some amount of HP
     * from the person who has attacked
     *
     * @return The amount of HP to reduce
     */
    public static int getToReduceHp() {
        return TO_REDUCE_HP;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard card, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard) {
        return false;
    }
}
