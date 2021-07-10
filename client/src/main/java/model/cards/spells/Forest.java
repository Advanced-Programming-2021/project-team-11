package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterType;

public class Forest extends FieldSpellCard {
    private final static String CARD_NAME = "Forest";
    private final static int ATTACK_DEFENCE_DELTA = 200;
    private final static MonsterType[] ATTACK_DEFENCE_TYPES = new MonsterType[]{MonsterType.BEAST, MonsterType.BEAST_WARRIOR, MonsterType.INSECT};
    private static Forest instance;

    private Forest() {
        super(CARD_NAME);
    }

    public static Forest getInstance() {
        if (instance == null)
            instance = new Forest();
        return instance;
    }

    @Override
    public int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard) {
        return super.isMonsterTypeSame(card, ATTACK_DEFENCE_TYPES) ? ATTACK_DEFENCE_DELTA : 0;
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        return getDefenceDelta(card, playerBoard); // have same effect
    }
}
