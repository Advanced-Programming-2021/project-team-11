package model.exceptions;

public class NoSuitableCardFoundException extends Exception {
    public NoSuitableCardFoundException() {
        super("no suitable card found");
    }
}
