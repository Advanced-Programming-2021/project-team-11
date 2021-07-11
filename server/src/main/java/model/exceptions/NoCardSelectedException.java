package model.exceptions;

public class NoCardSelectedException extends Exception {
    public NoCardSelectedException() {
        super("no card is selected yet");
    }
}
