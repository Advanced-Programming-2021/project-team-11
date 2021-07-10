package model.exceptions;

public class BooAnException extends RuntimeException {
    public BooAnException(String message) {
        super(message);
    }
    public BooAnException(Throwable ex) {
        super(ex);
    }
}
