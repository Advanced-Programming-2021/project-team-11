package model.exceptions;

public class PlayedYourselfException extends Exception {
    public PlayedYourselfException() {
        super("You have played yourself!");
    }
}
