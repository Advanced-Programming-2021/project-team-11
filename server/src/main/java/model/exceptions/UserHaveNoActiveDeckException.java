package model.exceptions;

import model.User;

public class UserHaveNoActiveDeckException extends Exception {
    public UserHaveNoActiveDeckException(User user) {
        super(String.format("%s has no active deck", user.getUsername()));
    }
}
