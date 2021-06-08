package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class YomiShip extends InitializableEffectMonsters {
    private static final String CARD_NAME = "Yomi Ship";
    private static YomiShip instance;

    private YomiShip() {
        super(CARD_NAME);
    }

    public static YomiShip getInstance() {
        if (instance == null)
            instance = new YomiShip();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return false;
    }
}
