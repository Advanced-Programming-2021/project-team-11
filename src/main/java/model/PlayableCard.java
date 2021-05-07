package model;

import model.cards.Card;
import model.cards.CardType;
import model.cards.MonsterCard;
import model.enums.CardPlaceType;

public class PlayableCard {
    private final Card card;
    private CardPlaceType cardPlace;
    private int attackDelta, defenceDelta;
    private boolean hidden = true, isAttacking, hasAttacked = false, changedPosition = false;

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
        changedPosition = true;
        isAttacking = !isAttacking;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking() {
        changedPosition = true;
        isAttacking = true;
    }

    public void setDefencing() {
        changedPosition = true;
        isAttacking = false;
    }

    public void flipSummon() {
        changedPosition = true;
        isAttacking = true;
    }

    public boolean isPositionChangedInThisTurn() {
        return changedPosition;
    }

    public void resetPositionChangedInThisTurn() {
        changedPosition = false;
    }

    public Card getCard() {
        return card;
    }

    public int getAttackPower() {
        if (card.getCardType() == CardType.MONSTER)
            return ((MonsterCard) card).getAttack() + attackDelta;
        return 0;
    }

    public int getDefencePower() {
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

    /**
     * How should rival see this card?
     *
     * @return The text to show
     */
    public String toRivalString() {
        switch (cardPlace) {
            case HAND:
                return "c";
            case FIELD:
                return "O";
            case MONSTER:
                if (isHidden())
                    return "DH";
                return isAttacking() ? "OO" : "DO";
            case SPELL:
                return isHidden() ? "H " : "O ";
        }
        return "";
    }
}