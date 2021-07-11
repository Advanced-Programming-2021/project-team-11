package model.exceptions;

import model.cards.Card;

public class DeckCardNotExistsException extends Exception {
    public DeckCardNotExistsException(String cardName, boolean isSide) {
        super(String.format("card with name %s does not exist in %s deck", cardName, isSide ? "side" : "main"));
    }

    public DeckCardNotExistsException(Card card, boolean isSide) {
        this(card.getName(), isSide);
    }
}
