package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;

public class SpellAbsorption extends SpellCard {
    private final static String CARD_NAME = "Spell Absorption";
    private final static int HEALTH_ADDED = 500;
    private static SpellAbsorption instance;

    private SpellAbsorption() {
        super(CARD_NAME, SpellCardType.CONTINUES, false);
    }

    public static SpellAbsorption getInstance() {
        if (instance == null)
            instance = new SpellAbsorption();
        return instance;
    }

    public static int getHealthAdded() {
        return HEALTH_ADDED;
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

    @Override
    public void throwConditionNotMadeException() {

    }
}
