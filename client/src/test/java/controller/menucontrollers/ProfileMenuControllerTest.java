package controller.menucontrollers;

import model.User;
import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ProfileMenuControllerTest {

    @BeforeEach
    void setUp() {
        User.getUsers().clear();
        new User("hirbod", "hirbod", "hirbod");
        new User("hirbod1", "hirbod1", "hirbod1");
    }

    @Test
    void changeNickname() {
        User user = null;
        try {
            user = LoginMenuController.login("hirbod", "hirbod");
        } catch (InvalidCredentialException e) {
            fail(e);
        }
        try {
            ProfileMenuController.changeNickname(user, "1");
        } catch (NicknameExistsException e) {
            fail(e);
        }
        try {
            ProfileMenuController.changeNickname(user, "hirbod1");
            fail("nickname change successful");
        } catch (NicknameExistsException ignored) {
        }
    }

    @Test
    void changePassword() {
        User user = null;
        try {
            user = LoginMenuController.login("hirbod", "hirbod");
        } catch (InvalidCredentialException e) {
            fail(e);
        }
        try {
            ProfileMenuController.changePassword(user, "hirbod", "hirbod2", "hirbod2");
        } catch (CurrentPasswordInvalidException | SameNewPasswordException | PasswordsDontMatchException e) {
            fail(e);
        }
        try {
            ProfileMenuController.changePassword(user, "hirbod2", "hirbod", "hirbod");
        } catch (CurrentPasswordInvalidException | SameNewPasswordException | PasswordsDontMatchException e) {
            fail(e);
        }
        try {
            ProfileMenuController.changePassword(user, "hirbod2", "hirbod2", "hirbod2");
        } catch (CurrentPasswordInvalidException ignored) {
        } catch (SameNewPasswordException | PasswordsDontMatchException e) {
            fail(e);
        }
        try {
            ProfileMenuController.changePassword(user, "hirbod", "hirbod", "hirbod");
        } catch (CurrentPasswordInvalidException | PasswordsDontMatchException e) {
            fail(e);
        } catch (SameNewPasswordException ignored) {
        }
    }
}
