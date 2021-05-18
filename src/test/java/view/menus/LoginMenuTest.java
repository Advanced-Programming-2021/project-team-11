package view.menus;

import model.User;
import org.junit.jupiter.api.*;
import view.menus.commands.user.UserLoginCommand;
import view.menus.commands.user.UserRegisterCommand;

public class LoginMenuTest {
    @BeforeAll
    static void setup() {
        Setuper.setup();
    }

    @BeforeEach
    void setUp() {
        User.getUsers().clear();
        Setuper.reset();
    }

    @AfterAll
    static void cleanUp() {
        User.getUsers().clear();
        Setuper.restore();
    }

    @Test
    void commandTest() {
        Assertions.assertTrue(new UserLoginCommand().isValid());
        Assertions.assertTrue(new UserRegisterCommand().isValid());
    }

    @Test
    void commandsTest() {
        Setuper.setInput("menu enter Login\n" +
                "menu show-current\n" +
                "menu enter Kir\n" +
                "menu enter Main\n" +
                "koobs\n" +
                "user create -u 2 -p 2 -n 2\n" +
                "user create -u 2 -p 2 -n 2\n" +
                "user login -u 2 -p 1\n" +
                "user login -u 2 -p 2\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
        Assertions.assertEquals("menu navigation is not possible\n" +
                "Login Menu\n" +
                "invalid command\n" +
                "please login first\n" +
                "invalid command\n" +
                "user created successfully!\n" +
                "user with username 2 already exists\n" +
                "Username and password didn't match!\n" +
                "user logged in successfully!\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
    }
}
