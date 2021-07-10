package model.exceptions;

public class NoCardSelectedYetException extends Exception {
    public NoCardSelectedYetException() {
        super("no card is selected yet");
    }
}
