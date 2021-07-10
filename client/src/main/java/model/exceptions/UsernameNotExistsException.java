package model.exceptions;

public class UsernameNotExistsException extends Exception {
    public UsernameNotExistsException() {
        super("there is no player with this username");
    }
}
