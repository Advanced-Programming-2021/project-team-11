package model.exceptions;

public class CurrentPasswordInvalidException extends Exception {
    public CurrentPasswordInvalidException() {
        super("current password is invalid");
    }
}
