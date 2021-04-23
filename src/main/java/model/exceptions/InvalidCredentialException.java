package model.exceptions;

public class InvalidCredentialException extends Exception {
    public InvalidCredentialException() {
        super("Username and password didn't match!");
    }
}
