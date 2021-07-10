package model.exceptions;

import model.User;

public class UserDeckIsInvalidException extends Exception {
    public UserDeckIsInvalidException(User user) {
        super(String.format("%s's deck is invalid", user.getUsername()));
    }
}
