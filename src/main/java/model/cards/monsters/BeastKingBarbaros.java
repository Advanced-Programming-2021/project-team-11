package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class BeastKingBarbaros extends InitializableEffectMonsters {
    private final static String CARD_NAME = "Beast King Barbaros";
    private static BeastKingBarbaros instance;
    private final static int REDUCED_ATTACK = 1900;

    private BeastKingBarbaros() {
        super(CARD_NAME);
    }

    public static BeastKingBarbaros getInstance() {
        if (instance == null)
            instance = new BeastKingBarbaros();
        return instance;
    }

    public static int getToReduceAttack() {
        return REDUCED_ATTACK - instance.getAttack();
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
