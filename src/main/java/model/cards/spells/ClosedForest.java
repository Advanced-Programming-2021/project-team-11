package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterCard;
import model.cards.MonsterType;

public class ClosedForest extends FieldSpellCard {
    private final static String CARD_NAME = "Closed Forest";
    private final static int ATTACK_DEFENCE_MULTIPLIER = 100;
    private final static MonsterType[] ATTACK_DEFENCE_TYPES = new MonsterType[]{MonsterType.BEAST, MonsterType.BEAST_WARRIOR};
    private static ClosedForest instance;

    private ClosedForest() {
        super(CARD_NAME);
    }

    public static ClosedForest getInstance() {
        if (instance == null)
            instance = new ClosedForest();
        return instance;
    }

    @Override
    public int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard) {
        if (super.isMonsterTypeSame(card, ATTACK_DEFENCE_TYPES))
            if (playerBoard.getMonsterCardsList().stream().anyMatch(c -> c == card)) // only if this players
                return ATTACK_DEFENCE_MULTIPLIER * (int) playerBoard.getGraveyard().stream().filter(c -> c.getCard() instanceof MonsterCard).count();
        return 0;
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        return getDefenceDelta(card, playerBoard); // have same effect
    }
}
