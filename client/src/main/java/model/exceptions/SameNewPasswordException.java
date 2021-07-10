package model.exceptions;

public class SameNewPasswordException extends Exception {
    public SameNewPasswordException() {
        super("please enter a new password");
    }
}
