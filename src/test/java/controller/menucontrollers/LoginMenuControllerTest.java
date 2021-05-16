package controller.menucontrollers;

import model.User;
import model.exceptions.InvalidCredentialException;
import model.exceptions.NicknameExistsException;
import model.exceptions.UsernameExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginMenuControllerTest {

    @BeforeEach
    void setUp() {
        User.getUsers().clear();
        new User("hirbod", "hirbod", "hirbod");
    }

    @Test
    void login() {
        // Ok test
        User user = null;
        try {
            user = LoginMenuController.login("hirbod", "hirbod");
        } catch (InvalidCredentialException e) {
            fail(e);
        }
        assertNotNull(user);
        // Invalid username test
        try {
            user = LoginMenuController.login("hirbod1", "hirbod");
            fail("found a user: " + user);
        } catch (InvalidCredentialException ignored) {
        }
        // Invalid password test
        try {
            user = LoginMenuController.login("hirbod", "hirbod1");
            fail("found a user: " + user);
        } catch (InvalidCredentialException ignored) {
        }
    }

    @Test
    void register() {
        try {
            LoginMenuController.register("hirbod1", "hirbod1", "1");
        } catch (UsernameExistsException | NicknameExistsException e) {
            fail(e);
        }
        try {
            assertNotNull(LoginMenuController.login("hirbod1", "hirbod1"));
        } catch (InvalidCredentialException e) {
            fail(e);
        }
        try {
            LoginMenuController.register("hirbod","hirbod","1");
            fail("registering hirbod user successful");
        } catch (UsernameExistsException ignored) {
        } catch (NicknameExistsException e) {
            fail("nickname must be checked after username");
        }
        try {
            LoginMenuController.register("hirbod","hirbod","1");
            fail("registering hirbod user successful");
        } catch (UsernameExistsException ignored) {
        } catch (NicknameExistsException e) {
            fail("nickname must be checked after username");
        }
        try {
            LoginMenuController.register("hirbodwtrwtw","2","1");
            fail("registering 1 user successful");
        } catch (UsernameExistsException e) {
            fail(e);
        } catch (NicknameExistsException ignored) {
        }
    }
}