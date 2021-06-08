package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class Marshmallon extends InitializableEffectMonsters {
    private final static String CARD_NAME = "Marshmallon";
    private final static int TO_REDUCE_HP = 1000;
    private static Marshmallon instance;

    private Marshmallon() {
        super(CARD_NAME);
    }

    public static Marshmallon getInstance() {
        if (instance == null)
            instance = new Marshmallon();
        return instance;
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
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
