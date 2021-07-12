package model.results;

import model.Deck;

public class DeckListTableResult {
    private String name, deckSummery;
    private int mainDeckSize, sideDeckSize;
    private boolean isValid, isActive;

    public DeckListTableResult() {

    }

    public DeckListTableResult(String name, Deck deck, boolean isActive) {
        this.name = name;
        this.deckSummery = deck.getSummery();
        this.mainDeckSize = deck.getMainDeck().size();
        this.sideDeckSize = deck.getSideDeck().size();
        this.isValid = deck.isValid();
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public int getMainDeckSize() {
        return mainDeckSize;
    }

    public int getSideDeckSize() {
        return sideDeckSize;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getDeckSummery() {
        return deckSummery;
    }
}
