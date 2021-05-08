package model;

import model.cards.Card;
import model.cards.CardType;
import model.cards.MonsterCard;
import model.cards.monsters.CommandKnight;
import model.enums.CardPlaceType;

import java.util.Arrays;

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

    public int getAttackPower(PlayerBoard myBoard) {
        if (card.getCardType() == CardType.MONSTER)
            return ((MonsterCard) card).getAttack() + getAttackDelta(myBoard);
        return 0;
    }

    public int getDefencePower(PlayerBoard myBoard) {
        int tempDelta = 0;
        if (card.getCardType() == CardType.MONSTER)
            return ((MonsterCard) card).getDefence() + getDefenceDelta(myBoard);
        return 0;
    }

    public int getAttackDelta(PlayerBoard myBoard) {
        int tempDelta = 0;
        if (Arrays.stream(myBoard.getMonsterCards()).anyMatch(card -> card.getCard() instanceof CommandKnight))
            tempDelta += CommandKnight.getAttackDelta();
        return attackDelta + tempDelta;
    }

    public int getDefenceDelta(PlayerBoard myBoard) {
        int tempDelta = 0;
        return defenceDelta + tempDelta;
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

    public void sendToGraveyard() {
        this.cardPlace = CardPlaceType.GRAVEYARD;
        hidden = false;
        hasAttacked = false;
        changedPosition = false;
        defenceDelta = 0;
        attackDelta = 0;
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