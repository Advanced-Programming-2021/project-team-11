package model.exceptions;

public class NoCardHereToAttackException extends Exception {
    public NoCardHereToAttackException() {
        super("there is no card to attack here");
    }
}
