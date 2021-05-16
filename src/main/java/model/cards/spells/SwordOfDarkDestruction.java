package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterType;

public class SwordOfDarkDestruction extends EquipSpellCard {
    private final static String CARD_NAME = "Sword of dark destruction";
    private final static int ATTACK_DELTA = 500;
    private final static int DEFENCE_DELTA = -200;
    private final static MonsterType[] ATTACK_DEFENCE_TYPES = new MonsterType[]{MonsterType.FIEND, MonsterType.SPELLCASTER};
    private static SwordOfDarkDestruction instance;

    private SwordOfDarkDestruction() {
        super(CARD_NAME);
    }

    public static SwordOfDarkDestruction getInstance() {
        if (instance == null)
            instance = new SwordOfDarkDestruction();
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
