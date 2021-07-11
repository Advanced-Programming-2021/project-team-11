package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;

public class UnitedWeStand extends EquipSpellCard {
    private final static String CARD_NAME = "United We Stand";
    private final static int ATTACK_DEFENCE_MULTIPLIER = 800;
    private static UnitedWeStand instance;

    private UnitedWeStand() {
        super(CARD_NAME);
    }

    public static UnitedWeStand getInstance() {
        if (instance == null)
            instance = new UnitedWeStand();
        return instance;
    }

    @Override
    public int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard) {
        return ATTACK_DEFENCE_MULTIPLIER * (int) playerBoard.getMonsterCardsList().stream().filter(c -> !c.isHidden()).count();
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        return getDefenceDelta(card, playerBoard); // have same effect
    }
}
