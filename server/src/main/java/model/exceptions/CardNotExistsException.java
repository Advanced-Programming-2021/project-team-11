package model.exceptions;

import model.cards.Card;

public class CardNotExistsException extends Exception {
    public CardNotExistsException(String cardName) {
        super(String.format("card with name %s does not exist", cardName));
    }

    public CardNotExistsException(Card card) {
        this(card.getName());
    }

    public CardNotExistsException(String message, String cardName) {
        super(String.format(message, cardName));
    }
}
