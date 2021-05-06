package model.exceptions;

public class NoCardFoundInPositionException extends Exception {
    public NoCardFoundInPositionException() {
        super("no card found in the given position");
    }
}
