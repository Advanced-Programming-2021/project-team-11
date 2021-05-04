package model;

import model.cards.Card;
import model.cards.CardType;
import model.cards.MonsterCard;
import model.enums.CardPlaceType;

public class PlayableCard {
    private final Card card;
    private CardPlaceType cardPlace;
    private int attackDelta, defenceDelta;
    private boolean hidden = true, attack, hasAttacked;

    public PlayableCard(Card card, CardPlaceType cardPlace) {
        this.card = card;
        this.cardPlace = cardPlace;
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

    public CardPlaceType getCardPlace() {
        return cardPlace;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void addDefenceDelta(int delta) {
        defenceDelta += delta;
    }

    public void addAttackDelta(int delta) {
        attackDelta += delta;
    }

    public void setCardPlace(CardPlaceType cardPlace) {
        this.cardPlace = cardPlace;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
}