package model.exceptions;

public class PasswordsDontMatchException extends Exception {
    public PasswordsDontMatchException() {
        super("Passwords do not match!");
    }
}
