package model;

import model.cards.Card;
import model.cards.CardType;
import model.cards.MonsterCard;
import model.cards.monsters.CommandKnight;
import model.cards.monsters.ScannerCard;
import model.cards.monsters.TheCalculator;
import model.cards.spells.EquipSpellCard;
import model.cards.spells.FieldSpellCard;
import model.cards.spells.SpellAbsorption;
import model.enums.CardPlaceType;

public class PlayableCard {
    private final Card card;
    private CardPlaceType cardPlace;
    private int attackDelta, defenceDelta, effectActivateCounterTotal = 0, effectActivateCounterRound = 0;
    private boolean hidden = true, isAttacking, hasAttacked = false, changedPosition = false, spellActivated = false;
    /**
     * This is only a temp card to mimic for when the card is {@link model.cards.monsters.ScannerCard}
     */
    private Card mimicCard;
    private EquipSpellCard equippedCard;

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
        if (this.card instanceof ScannerCard && mimicCard != null)
            return mimicCard;
        return card;
    }

    public int getAttackPower(PlayerBoard myBoard, PlayableCard field) {
        if (getCard().getCardType() == CardType.MONSTER)
            return ((MonsterCard) getCard()).getAttack() + getAttackDelta(myBoard)
                    + getFieldEffectForAttack(myBoard, field) + getEquippedCardAttackDiff(myBoard);
        return 0;
    }

    public int getDefencePower(PlayerBoard myBoard, PlayableCard field) {
        if (getCard().getCardType() == CardType.MONSTER)
            return ((MonsterCard) getCard()).getDefence() + getDefenceDelta(myBoard)
                    + getFieldEffectForDefence(myBoard, field) + getEquippedCardDefenceDiff(myBoard);
        return 0;
    }

    public int getAttackDeltaRaw() {
        return attackDelta;
    }

    private int getAttackDelta(PlayerBoard myBoard) {
        int tempDelta = 0;
        if (myBoard.getMonsterCardsList().stream().anyMatch(card -> card.getCard() instanceof CommandKnight && card.isAttacking()))
            tempDelta += CommandKnight.getAttackDelta();
        if (getCard() instanceof TheCalculator)
            tempDelta += myBoard.getMonsterCardsList().stream().mapToInt(card -> ((MonsterCard) card.getCard()).getLevel()).sum() * TheCalculator.getAttackMultiplier();
        return getAttackDeltaRaw() + tempDelta;
    }

    private int getFieldEffectForAttack(PlayerBoard myBoard, PlayableCard field) {
        if (field != null)
            return ((FieldSpellCard) field.getCard()).getAttackDelta(this, myBoard);
        return 0;
    }

    private int getFieldEffectForDefence(PlayerBoard myBoard, PlayableCard field) {
        if (field != null)
            return ((FieldSpellCard) field.getCard()).getDefenceDelta(this, myBoard);
        return 0;
    }

    private int getEquippedCardAttackDiff(PlayerBoard myBoard) {
        if (equippedCard != null)
            return equippedCard.getAttackDelta(this, myBoard);
        return 0;
    }

    private int getEquippedCardDefenceDiff(PlayerBoard myBoard) {
        if (equippedCard != null)
            return equippedCard.getDefenceDelta(this, myBoard);
        return 0;
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

    public boolean hasEffectActivated() {
        return spellActivated;
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

    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard rivalCard) {
        effectActivateCounterRound++;
        effectActivateCounterTotal++;
        spellActivated = true;
        getCard().activateEffect(myBoard, rivalBoard, this, rivalCard, 0);
        checkSpellAbsorption(myBoard, rivalBoard);
    }

    public void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard rivalCard, boolean isTotalTimeActivatedImportant) {
        effectActivateCounterRound++;
        effectActivateCounterTotal++;
        spellActivated = true;
        getCard().activateEffect(myBoard, rivalBoard, this, rivalCard, isTotalTimeActivatedImportant ? effectActivateCounterTotal : effectActivateCounterRound);
        checkSpellAbsorption(myBoard, rivalBoard);
    }

    public boolean isEffectConditionMet(PlayerBoard myBoard, PlayerBoard rivalBoard) {
        return getCard().isConditionMade(myBoard, rivalBoard, this, 0);
    }

    public boolean isEffectConditionMet(PlayerBoard myBoard, PlayerBoard rivalBoard, boolean isTotalTimeActivatedImportant) {
        return getCard().isConditionMade(myBoard, rivalBoard, this, isTotalTimeActivatedImportant ? effectActivateCounterTotal : effectActivateCounterRound);
    }

    public void setMimicCard(Card mimicCard) {
        this.mimicCard = mimicCard;
    }

    void sendToGraveyard() {
        this.mimicCard = null;
        this.cardPlace = CardPlaceType.GRAVEYARD;
        this.hidden = false;
        this.hasAttacked = false;
        this.changedPosition = false;
        this.defenceDelta = 0;
        this.attackDelta = 0;
        this.equippedCard = null;
    }

    public void resetEffectActivateCounterRound() {
        resetSpellActivated();
        this.effectActivateCounterRound = 0;
    }

    public void resetSpellActivated() {
        this.spellActivated = false;
    }

    public void setEquippedCard(EquipSpellCard equipCard) {
        this.equippedCard = equipCard;
    }

    public EquipSpellCard getEquippedCard() {
        return this.equippedCard;
    }

    private void checkSpellAbsorption(PlayerBoard board1, PlayerBoard board2) {
        if (board1 != null && board1.getSpellCardsList().stream().anyMatch(c -> !c.isHidden() && c.getCard() instanceof SpellAbsorption))
            board1.getPlayer().increaseHealth(SpellAbsorption.getHealthAdded());
        if (board2 != null && board2.getSpellCardsList().stream().anyMatch(c -> !c.isHidden() && c.getCard() instanceof SpellAbsorption))
            board2.getPlayer().increaseHealth(SpellAbsorption.getHealthAdded());
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