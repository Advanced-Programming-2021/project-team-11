package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class HeraldOfCreation extends InitializableEffectMonsters {
    private static HeraldOfCreation instance;
    private final static String CARD_NAME = "Herald of Creation";

    private HeraldOfCreation() {
        super(CARD_NAME);
    }

    public static HeraldOfCreation getInstance() {
        if (instance == null)
            instance = new HeraldOfCreation();
        return instance;
    }

    public static String getCardName() {
        return CARD_NAME;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !thisCard.hasEffectActivated();
    }
}