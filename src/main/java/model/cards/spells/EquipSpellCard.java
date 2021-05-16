package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCardType;

public abstract class EquipSpellCard extends EquipAndFieldCards {
    EquipSpellCard(String name) {
        super(name, SpellCardType.EQUIP, true);
    }

    @Override
    public final boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return !myBoard.isMonsterZoneEmpty();
    }
}
