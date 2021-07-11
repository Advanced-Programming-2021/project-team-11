package model.exceptions;

public class UsernameExistsException extends Exception {
    public UsernameExistsException(String username) {
        super(String.format("user with username %s already exists", username));
    }
}
