package model.cards.spells;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterCard;
import model.cards.MonsterType;
import model.cards.SpellCard;
import model.cards.SpellCardType;

import java.util.Arrays;

public abstract class EquipAndFieldCards extends SpellCard {

    public EquipAndFieldCards(String name, SpellCardType spellCardType, boolean needsUserInteraction) {
        super(name, spellCardType, needsUserInteraction);
    }

    protected final boolean isMonsterTypeSame(PlayableCard card, MonsterType[] expectedTypes) {
        if (!(card.getCard() instanceof MonsterCard))
            return false;
        return Arrays.stream(expectedTypes).anyMatch(type -> ((MonsterCard) card.getCard()).getMonsterType() == type);
    }

    public abstract int getDefenceDelta(PlayableCard card, PlayerBoard playerBoard);

    public abstract int getAttackDelta(PlayableCard card, PlayerBoard playerBoard);

    @Override
    public final void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter) {

    }

    @Override
    public final void deactivateEffect() {

    }

    @Override
    public final void throwConditionNotMadeException() {

    }
}
