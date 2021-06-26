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
        return super.isMonsterTypeSame(card, ATTACK_DEFENCE_TYPES) ? DEFENCE_DELTA : 0;
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        return super.isMonsterTypeSame(card, ATTACK_DEFENCE_TYPES) ? ATTACK_DELTA : 0;
    }
}
