package model.results;

import model.cards.Card;
import model.enums.AttackResult;

public class MonsterAttackResult {
    private final int damageReceived;
    private final boolean isHidden, wasAttackCard;
    private final String cardName;
    private final AttackResult result;

    public MonsterAttackResult(int damageReceived, boolean wasCardHidden, boolean wasCardModeAttack, Card attackedCard, AttackResult attackResult) {
        this.damageReceived = damageReceived;
        this.isHidden = wasCardHidden;
        this.wasAttackCard = wasCardModeAttack;
        this.cardName = attackedCard.getName();
        this.result = attackResult;
    }

    @Override
    public String toString() {
        String resultString = "";
        if (isHidden)
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
