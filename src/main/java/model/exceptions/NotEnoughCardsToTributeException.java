package model.exceptions;

public class NotEnoughCardsToTributeException extends Exception {
    public NotEnoughCardsToTributeException() {
        super("there are not enough cards for tribute");
    }
}
