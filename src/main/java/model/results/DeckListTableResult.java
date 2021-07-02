package model.results;

import model.Deck;

public class DeckListTableResult {
    private final String name;
    private final int mainDeckSize, sideDeckSize;
    private final boolean isValid, isActive;

    public DeckListTableResult(String name, Deck deck, boolean isActive) {
        this.name = name;
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
}
