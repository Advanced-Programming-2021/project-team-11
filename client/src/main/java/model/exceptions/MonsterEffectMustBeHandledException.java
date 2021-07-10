package model.exceptions;

import model.PlayableCard;

/**
 * This is a special case exception which tells view that the selected card monster spell must be handled
 * in view. For example the {@link model.cards.monsters.ScannerCard} must have to get one card from rivals graveyard
 * which must be handled in view
 */
public class MonsterEffectMustBeHandledException extends Exception {
    private final PlayableCard card;

    public MonsterEffectMustBeHandledException(PlayableCard card) {
        this.card = card;
    }

    public PlayableCard getCard() {
        return card;
    }
}
