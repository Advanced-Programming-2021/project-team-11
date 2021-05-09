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
    private int attackDelta, defenceDelta, effectActivateCounterTotal = 0, effectActivateCounterRound = 0;
    private boolean hidden = true, isAttacking, hasAttacked = false, changedPosition = false;

    public PlayableCard(Card card, CardPlaceType cardPlace) {
        this.card = card;
        this.cardPlace = cardPlace;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void makeVisible() {
        this.hidden = false;
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
        makeVisible();
        setAttacking();
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
        if (card.getCardType() == CardType.MONSTER)
            return ((MonsterCard) card).getDefence() + getDefenceDelta(myBoard);
        return 0;
    }

    public int getAttackDeltaRaw() {
        return attackDelta;
    }

    private int getAttackDelta(PlayerBoard myBoard) {
        int tempDelta = 0;
        if (myBoard.getMonsterCardsList().stream().anyMatch(card -> card.getCard() instanceof CommandKnight && card.isAttacking()))
            tempDelta += CommandKnight.getAttackDelta();
        return getAttackDeltaRaw() + tempDelta;
    }

    public int getDefenceDeltaRaw() {
        return defenceDelta;
    }

    private int getDefenceDelta(PlayerBoard myBoard) {
        int tempDelta = 0;
        return getDefenceDeltaRaw() + tempDelta;
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

    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard card) {
        effectActivateCounterRound++;
        effectActivateCounterTotal++;
        getCard().activateEffect(myBoard, rivalBoard, card, 0);
    }

    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard card, boolean isTotalTimeActivatedImportant) {
        effectActivateCounterRound++;
        effectActivateCounterTotal++;
        getCard().activateEffect(myBoard, rivalBoard, card, isTotalTimeActivatedImportant ? effectActivateCounterTotal : effectActivateCounterRound);
    }

    public boolean isEffectConditionMet(PlayerBoard myBoard, PlayerBoard rivalBoard) {
        return getCard().isConditionMade(myBoard, rivalBoard, 0);
    }

    public boolean isEffectConditionMet(PlayerBoard myBoard, PlayerBoard rivalBoard, boolean isTotalTimeActivatedImportant) {
        return getCard().isConditionMade(myBoard, rivalBoard, isTotalTimeActivatedImportant ? effectActivateCounterTotal : effectActivateCounterRound);
    }

    void sendToGraveyard() {
        this.cardPlace = CardPlaceType.GRAVEYARD;
        hidden = false;
        hasAttacked = false;
        changedPosition = false;
        defenceDelta = 0;
        attackDelta = 0;
    }

    /**
     * How should player see this card?
     *
     * @return A string which says how
     */
    @Override
    public String toString() {
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