package model.exceptions;

public class CantActivateSpellException extends Exception {
    public CantActivateSpellException() {
        super("can't activate spell");
    }
}
