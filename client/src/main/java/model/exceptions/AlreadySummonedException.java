package model.exceptions;

public class AlreadySummonedException extends Exception {
    public AlreadySummonedException() {
        super("you already summoned/set on this turn");
    }
}
