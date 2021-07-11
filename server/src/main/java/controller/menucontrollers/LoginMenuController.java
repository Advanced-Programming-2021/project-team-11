package controller.menucontrollers;

import model.User;
import model.exceptions.InvalidCredentialException;
import model.exceptions.NicknameExistsException;
import model.exceptions.PasswordsDontMatchException;
import model.exceptions.UsernameExistsException;

public class LoginMenuController {
    public static User login(String username, String password) throws InvalidCredentialException {
        User user = User.getUserByUsername(username);
        if (user == null || !user.checkPassword(password))
            throw new InvalidCredentialException();
        return user;
    }

    public static void register(String username, String password, String passwordConfirm, String nickname) throws UsernameExistsException, NicknameExistsException, PasswordsDontMatchException {
        if (!password.equals(passwordConfirm))
            throw new PasswordsDontMatchException();
        if (User.getUserByUsername(username) != null)
            throw new UsernameExistsException(username);
        if (User.getUserByNickname(nickname) != null)
            throw new NicknameExistsException(nickname);
        new User(username, password, nickname);
    }
}
