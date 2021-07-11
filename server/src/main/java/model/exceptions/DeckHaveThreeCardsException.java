package model.exceptions;

import model.cards.Card;

public class DeckHaveThreeCardsException extends Exception {
    public DeckHaveThreeCardsException(Card card, String deckName) {
        super(String.format("there are already three cards with name %s in deck %s", card.getName(), deckName));
    }
}
