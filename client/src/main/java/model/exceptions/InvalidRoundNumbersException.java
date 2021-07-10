package model.exceptions;

public class InvalidRoundNumbersException extends Exception {
    public InvalidRoundNumbersException() {
        super("number of rounds is not supported");
    }
}
