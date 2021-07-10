package model.exceptions;

public class NicknameExistsException extends Exception {
    public NicknameExistsException(String nickname) {
        super(String.format("user with nickname %s already exists", nickname));
    }
}
