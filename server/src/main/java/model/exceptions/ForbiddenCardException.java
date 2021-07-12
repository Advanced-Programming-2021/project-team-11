package model.exceptions;

public class ForbiddenCardException extends Exception {
    public ForbiddenCardException() {
        super("this card is forbidden");
    }
}
