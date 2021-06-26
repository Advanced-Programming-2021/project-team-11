package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCard;
import model.cards.SpellCardType;
import model.exceptions.CantUseSpellException;

public class SwordsOfRevealingLight extends SpellCard {
    private final static String CARD_NAME = "Swords of Revealing Light";
    private static SwordsOfRevealingLight instance;

    private SwordsOfRevealingLight() {
        super(CARD_NAME, SpellCardType.NORMAL, false);
    }

    public static SwordsOfRevealingLight getInstance() {
        if (instance == null)
            instance = new SwordsOfRevealingLight();
        return instance;
    }

    @Override
    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {
        rivalBoard.activateSwordOfRevealingLight();
        rivalBoard.getMonsterCardsList().forEach(card -> {
            card.setAttacking();
            card.makeVisible();
        });
    }

    @Override
    public void deactivateEffect() {}

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !rivalBoard.isEffectOfSwordOfRevealingLightActive();
    }

    @Override
    public void throwConditionNotMadeException() throws CantUseSpellException {
        throw new CantUseSpellException("rival is already in effect of this card!");
    }
}
