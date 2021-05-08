package model.results;

import model.cards.Card;
import model.enums.AttackResult;

public class MonsterAttackResult {
    private final int damageReceived;
    /**
     * Was the card which we attacked to hidden?
     */
    private final boolean wasHidden;
    /**
     * Was the card which we attacked to in attack mode?
     */
    private final boolean wasAttackCard;
    /**
     * The card name which we attacked to
     */
    private final String cardName;
    private final AttackResult result;

    public MonsterAttackResult(int damageReceived, boolean wasCardHidden, boolean wasCardModeAttack, Card attackedCard, AttackResult attackResult) {
        this.damageReceived = damageReceived;
        this.wasHidden = wasCardHidden;
        this.wasAttackCard = wasCardModeAttack;
        this.cardName = attackedCard.getName();
        this.result = attackResult;
    }

    public int getDamageReceived() {
        return damageReceived;
    }

    /**
     * Get the card name which we attacked to
     *
     * @return The card name which we attacked to
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Get the final result of battle
     *
     * @return The result of battle
     */
    public AttackResult getBattleResult() {
        return result;
    }

    @Override
    public String toString() {
        String resultString = "";
        if (wasHidden)
            resultString += String.format("opponent’s monster card was %s and ", cardName);
        switch (result) {
            case RIVAL_DESTROYED:
                if (wasAttackCard)
                    resultString += String.format("your opponent's monster is destroyed and your opponent receives %d battle damage", damageReceived);
                else
                    resultString += "the defense position monster is destroyed";
                break;
            case DRAW:
                if (wasAttackCard)
                    resultString += "both you and your opponent monster cards are destroyed and no one receives damage";
                else
                    resultString += "no card is destroyed";
                break;
            case ME_DESTROYED:
                if (wasAttackCard)
                    resultString += String.format("your monster card is destroyed and you received %d battle damage", damageReceived);
                else
                    resultString += String.format("no card is destroyed and you received %d battle damage", damageReceived);
                break;
        }
        return resultString;
    }
}
