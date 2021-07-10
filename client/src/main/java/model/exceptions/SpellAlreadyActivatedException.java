package model.exceptions;

public class SpellAlreadyActivatedException extends Exception {
    public SpellAlreadyActivatedException() {
        super("spell already activated");
    }
}
