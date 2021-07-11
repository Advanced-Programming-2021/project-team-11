package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class MirageDragon extends InitializableEffectMonsters {
    private final static String CARD_NAME = "Mirage Dragon";
    private static MirageDragon instance;

    private MirageDragon() {
        super(CARD_NAME);
    }

    public static MirageDragon getInstance() {
        if (instance == null)
            instance = new MirageDragon();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !thisCard.isHidden();
    }
}
