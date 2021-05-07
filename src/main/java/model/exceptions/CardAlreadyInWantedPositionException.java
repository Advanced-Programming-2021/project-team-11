package model.exceptions;

public class CardAlreadyInWantedPositionException extends Exception {
    public CardAlreadyInWantedPositionException() {
        super("this card is already in the wanted position");
    }
}
