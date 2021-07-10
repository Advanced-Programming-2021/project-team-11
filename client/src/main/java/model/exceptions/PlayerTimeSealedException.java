package model.exceptions;

public class PlayerTimeSealedException extends Exception {
    public PlayerTimeSealedException() {
        super("you have been time sealed!");
    }
}
