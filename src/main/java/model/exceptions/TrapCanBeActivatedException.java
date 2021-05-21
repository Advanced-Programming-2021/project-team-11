package model.exceptions;

public class TrapCanBeActivatedException extends Exception {
    private final String[] allowedCards;

    public TrapCanBeActivatedException(String[] allowedCards) {
        this.allowedCards = allowedCards;
    }

    public String[] getAllowedCards() {
        return allowedCards;
    }
}
