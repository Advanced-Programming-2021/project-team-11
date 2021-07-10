package model.exceptions;

import model.cards.Card;

public class TributeNeededException extends Exception {
    private final int neededTributes;
    private final Card card;

    public TributeNeededException(Card card, int neededTributes) {
        super(String.format("%d tribute(s) needed", neededTributes));
        this.neededTributes = neededTributes;
        this.card = card;
    }

    public int getNeededTributes() {
        return neededTributes;
    }

    public Card getCard() {
        return card;
    }
}
