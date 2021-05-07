package model.exceptions;

public class CantChangeCardPositionException extends Exception {
    public CantChangeCardPositionException() {
        super("you can't change this card position");
    }
}
