package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.SpellCardType;

public abstract class FieldSpellCard extends EquipAndFieldCards {
    FieldSpellCard(String name) {
        super(name, SpellCardType.FIELD, false);
    }

    @Override
    public final boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter) {
        return true;
    }
}
