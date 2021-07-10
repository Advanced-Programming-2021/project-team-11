package model.exceptions;

public class CantSetCardException extends Exception {
    public CantSetCardException() {
        super("you can’t set this card");
    }
}
