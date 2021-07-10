package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;

public class BlackPendant extends EquipSpellCard {
    private final static String CARD_NAME = "Black Pendant";
    private final static int ATTACK_DELTA = 500;
    private static BlackPendant instance;

    private BlackPendant() {
        super(CARD_NAME);
    }

    public static BlackPendant getInstance() {
        if (instance == null)
            instance = new BlackPendant();
        return instance;
    }

    @Override
    public int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard) {
        return 0;
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        return ATTACK_DELTA;
    }
}
