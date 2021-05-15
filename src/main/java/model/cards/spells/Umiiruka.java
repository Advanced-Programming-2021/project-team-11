package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterType;

public class Umiiruka extends FieldSpellCard {
    private final static String CARD_NAME = "Umiiruka";
    private final static int ATTACK_DELTA = 500;
    private final static int DEFENCE_DELTA = -400;
    private final static MonsterType[] ATTACK_DEFENCE_TYPES = new MonsterType[]{MonsterType.AQUA};
    private static Umiiruka instance;

    private Umiiruka() {
        super(CARD_NAME);
    }

    public static Umiiruka getInstance() {
        if (instance == null)
            instance = new Umiiruka();
        return instance;
    }

    @Override
    public int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard) {
        if (super.isMonsterTypeSame(card, ATTACK_DEFENCE_TYPES))
            return DEFENCE_DELTA;
        return 0;
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        if (super.isMonsterTypeSame(card, ATTACK_DEFENCE_TYPES))
            return ATTACK_DELTA;
        return 0;
    }
}
