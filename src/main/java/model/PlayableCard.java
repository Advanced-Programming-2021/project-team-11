package model;

import model.cards.Card;
import model.cards.CardType;
import model.cards.MonsterCard;

public class PlayableCard {
    private final Card card;
    private boolean hidden = true, attack;
    private int attackDelta, defenceDelta;

    public PlayableCard(Card card) {
        this.card = card;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void makeVisible() {
        this.hidden = true;
    }

    public void swapAttackMode() {
        attack = !attack;
    }

    public boolean isAttacking() {
        return attack;
    }

    public Card getCard() {
        return card;
    }

    public int getAttack() {
        if (card.getCardType() == CardType.MONSTER)
            return ((MonsterCard) card).getAttack() + attackDelta;
        return 0;
    }

    public int getDefence() {
        if (card.getCardType() == CardType.MONSTER)
            return ((MonsterCard) card).getDefence() + defenceDelta;
        return 0;
    }

    public int getAttackDelta() {
        return attackDelta;
    }

    public int getDefenceDelta() {
        return defenceDelta;
    }

    public void addDefenceDelta(int delta) {
        defenceDelta += delta;
    }

    public void addAttackDelta(int delta) {
        attackDelta += delta;
    }
}
