package model.exceptions;

import model.cards.Card;

public class NotEnoughCardsToTributeException extends Exception {
    private final Card card;

    public NotEnoughCardsToTributeException(Card card) {
        super("there are not enough cards for tribute");
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
