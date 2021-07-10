package model.exceptions;

public class CardHiddenException extends Exception {
    public CardHiddenException() {
        super("card is not visible");
    }
}
