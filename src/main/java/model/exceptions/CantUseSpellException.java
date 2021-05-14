package model.exceptions;

public class CantUseSpellException extends Exception {
    public CantUseSpellException() {
        super("cant use this spell right now");
    }

    public CantUseSpellException(String message) {
        super("cant use this spell right now: " + message);
    }
}
