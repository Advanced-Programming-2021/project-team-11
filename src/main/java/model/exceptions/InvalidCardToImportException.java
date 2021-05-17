package model.exceptions;

public class InvalidCardToImportException extends Exception {
    public InvalidCardToImportException() {
        super("The card you are trying to import is not valid");
    }

    public InvalidCardToImportException(String what) {
        super("The card you are trying to import is not valid: " + what);
    }
}
