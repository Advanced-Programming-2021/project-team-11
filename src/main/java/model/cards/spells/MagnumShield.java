package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;

public class MagnumShield extends EquipSpellCard {
    private final static String CARD_NAME = "Magnum Shield";
    private static MagnumShield instance;

    private MagnumShield() {
        super(CARD_NAME);
    }

    public static MagnumShield getInstance() {
        if (instance == null)
            instance = new MagnumShield();
        return instance;
    }

    @Override
    public int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard) {
        if (!card.isAttacking())
            return card.getAttackPower(playerBoard, null); // :|
        return 0;
    }

    @Override
    public int getAttackDelta(PlayableCard card, PlayerBoard playerBoard) {
        if (card.isAttacking())
            return card.getDefencePower(playerBoard, null); // :|
        return 0;
    }
}
