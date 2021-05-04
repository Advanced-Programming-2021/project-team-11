package model.exceptions;

public class InvalidPhaseActionException extends Exception {
    public InvalidPhaseActionException() {
        super("you can't do this action in this phase");
    }
}
