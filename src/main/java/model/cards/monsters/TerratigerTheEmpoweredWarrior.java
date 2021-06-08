package model.cards.monsters;

import model.PlayableCard;
import model.PlayerBoard;

public class TerratigerTheEmpoweredWarrior extends InitializableEffectMonsters {
    private final static String CARD_NAME = "Terratiger, the Empowered Warrior";
    private static TerratigerTheEmpoweredWarrior instance;

    private TerratigerTheEmpoweredWarrior() {
        super(CARD_NAME);
    }

    public static TerratigerTheEmpoweredWarrior getInstance() {
        if (instance == null)
            instance = new TerratigerTheEmpoweredWarrior();
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
