package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

// TODO: when creating traps, check for this card

public class MirageDragon extends EffectMonsters {
    private final static String CARD_NAME = "Mirage Dragon";
    private static MirageDragon instance;

    private MirageDragon() {
        super(CARD_NAME);
    }

    public static void makeInstance() {
        if (instance == null)
            instance = new MirageDragon();
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public void deactivateEffect() {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !thisCard.isHidden();
    }
}
