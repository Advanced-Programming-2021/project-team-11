package model.exceptions;

public class DeckSideOrMainFullException extends Exception {
    public DeckSideOrMainFullException(boolean isSide) {
        super((isSide ? "side" : "main") + " deck is full");
    }
}
