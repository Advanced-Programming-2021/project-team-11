package model;

import model.cards.Card;

import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> mainDeck;
    private final ArrayList<Card> sideDeck;

    public Deck() {
        mainDeck = new ArrayList<>();
        sideDeck = new ArrayList<>();
    }

    public void addCardToMainDeck(Card card) {
        mainDeck.add(card);
    }

    public void addCardToSideDeck(Card card) {
        sideDeck.add(card);
    }

    public void removeCardFromMainDeck(Card card) {
        mainDeck.remove(card);
    }

    public void removeCardFromSideDeck(Card card) {
        sideDeck.remove(card);
    }

    public ArrayList<Card> getSideDeck() {
        return sideDeck;
    }

    public ArrayList<Card> getMainDeck() {
        return mainDeck;
    }

    public boolean isMainFull() {
        return mainDeck.size() >= 60;
    }

    public boolean isSideFull() {
        return sideDeck.size() >= 15;
    }

    public boolean isValid() {
        return mainDeck.size() >= 40;
    }

    public int countNumberOfCardsInTotal(Card card) {
        int counter = 0;
        for (Card cardInDeck : mainDeck)
            if (card.equals(cardInDeck))
                counter++;
        for (Card cardInDeck : sideDeck)
            if (card.equals(cardInDeck))
                counter++;
        return counter;
    }

    public boolean haveThreeCards(Card card) {
        return countNumberOfCardsInTotal(card) >= 3;
    }

}
