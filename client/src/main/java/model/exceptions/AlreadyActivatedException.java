package model.exceptions;

public class AlreadyActivatedException extends Exception {
    public AlreadyActivatedException() {
        super("you have already activated this card");
    }
}
