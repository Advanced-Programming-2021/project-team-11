package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterType;

public class Yami extends FieldSpellCard {
    private final static String CARD_NAME = "Yami";
    private final static int POSITIVE_ATTACK_DEFENCE_DELTA = 200;
    private final static MonsterType[] POSITIVE_ATTACK_DEFENCE_TYPES = new MonsterType[]{MonsterType.FIEND, MonsterType.SPELLCASTER};
    private final static int NEGATIVE_ATTACK_DEFENCE_DELTA = -200;
    private final static MonsterType[] NEGATIVE_ATTACK_DEFENCE_TYPES = new MonsterType[]{MonsterType.FAIRY};
    private static Yami instance;

    private Yami() {
        super(CARD_NAME);
    }

    public static Yami getInstance() {
        if (instance == null)
            instance = new Yami();
        return instance;
    }

    @Override
    public int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard) {
        if (super.isMonsterTypeSame(card, POSITIVE_ATTACK_DEFENCE_TYPES))
            return POSITIVE_ATTACK_DEFENCE_DELTA;
        if (super.isMonsterTypeSame(card, NEGATIVE_ATTACK_DEFENCE_TYPES))
            return NEGATIVE_ATTACK_DEFENCE_DELTA;
        return 0;
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        return getDefenceDelta(card, playerBoard); // have same effect
    }
}
