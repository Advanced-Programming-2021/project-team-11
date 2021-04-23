package controller.menucontrollers;

import model.User;
import model.exceptions.CurrentPasswordInvalidException;
import model.exceptions.NicknameExistsException;
import model.exceptions.SameNewPasswordException;

public class ProfileMenuController {
    public static void changeNickname(User user, String newNickname) throws NicknameExistsException {
        if (User.getUserByNickname(newNickname) != null)
            throw new NicknameExistsException(newNickname);
        user.setNickname(newNickname);
    }

    public static void changePassword(User user, String oldPassword, String newPassword) throws CurrentPasswordInvalidException, SameNewPasswordException {
        if (!user.checkPassword(oldPassword))
            throw new CurrentPasswordInvalidException();
        if (oldPassword.equals(newPassword))
            throw new SameNewPasswordException();
        user.setPassword(newPassword);
    }
}
