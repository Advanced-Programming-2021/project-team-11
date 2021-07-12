package model.results;

import model.Deck;

import java.util.TreeSet;

public class GetDecksResult {
    private DeckResult activeDeck;
    private TreeSet<DeckResult> otherDeck;

    public GetDecksResult() {

    }

    public GetDecksResult(DeckResult activeDeck, TreeSet<DeckResult> otherDeckNames) {
        this.activeDeck = activeDeck;
        this.otherDeck = otherDeckNames;
    }

    public DeckResult getActiveDeck() {
        return activeDeck;
    }

    public TreeSet<DeckResult> getOtherDecks() {
        return otherDeck;
    }

    public static class DeckResult implements Comparable<DeckResult> {
        private String name;
        private boolean isValid;
        private int sideCount, mainCount;

        public DeckResult() {

        }

        public DeckResult(String name, Deck deck) {
            this.name = name;
            isValid = deck.isValid();
            sideCount = deck.getSideDeck().size();
            mainCount = deck.getMainDeck().size();
        }

        public String getName() {
            return name;
        }

        public int getMainCount() {
            return mainCount;
        }

        public int getSideCount() {
            return sideCount;
        }

        public boolean isValid() {
            return isValid;
        }

        @Override
        public int compareTo(@org.jetbrains.annotations.NotNull DeckResult o) {
            return this.name.compareTo(o.getName());
        }

        @Override
        public String toString() {
            return String.format("%s: main deck %d, side deck %d, %s", getName(), getMainCount(), getSideCount(), isValid() ? "valid" : "invalid");
        }
    }
}