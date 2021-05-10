package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class BeastKingBarbaros extends EffectMonsters {
    private final static String CARD_NAME = "Beast King Barbaros";
    private static BeastKingBarbaros instance;
    private final static int REDUCED_ATTACK = 1900;

    private BeastKingBarbaros() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new BeastKingBarbaros();
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    public static int getToReduceAttack() {
        return REDUCED_ATTACK - instance.getAttack();
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
