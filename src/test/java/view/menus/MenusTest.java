package view.menus;

import model.User;
import model.cards.MonsterAttributeType;
import model.cards.MonsterCard;
import model.cards.MonsterType;
import model.cards.SimpleMonster;
import org.junit.jupiter.api.*;
import view.menus.commands.shop.ShopBuyItemCommand;
import view.menus.commands.user.UserLoginCommand;
import view.menus.commands.user.UserRegisterCommand;

public class MenusTest {
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
        Assertions.assertTrue(new ShopBuyItemCommand().isValid());
    }

    @Test
    void mainMenuTest() {
        Setuper.setInput("menu enter Login\n" +
                "menu show-current\n" +
                "menu enter Kir\n" +
                "menu enter Main\n" +
                "koobs\n" +
                "user create -u 2 -p 2 -n 2\n" +
                "user create -u 2 -p 2 -n 2\n" +
                "user login -u 2 -p 1\n" +
                "user login -u 2 -p 2\n" +
                "menu show-current\n" +
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
                "Main Menu\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
    }

    @Test
    void profileTest() {
        Setuper.setInput("user create -u 1 -p 1 -n 1\n" +
                "user create -u 2 -p 1 -n 2\n" +
                "user login -u 1 -p 1\n" +
                "menu enter Profile\n" +
                "menu show-current\n" +
                "menu enter Main\n" +
                "wgopwgpw\n" +
                "profile change --nickname 2\n" +
                "profile change --nickname 3\n" +
                "profile change --password --current 1 --new 1234\n" +
                "profile change\n" +
                "profile change --password --current 1 --new 1\n" +
                "profile change --password --current 1 --new 1 -nickname 3\n" +
                "profile change --password --new 1\n" +
                "profile change --password --current 1\n" +
                "profile change --password --current 1 --new 1234\n" +
                "menu exit\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
        Assertions.assertEquals("user created successfully!\n" +
                "user created successfully!\n" +
                "user logged in successfully!\n" +
                "Profile Menu\n" +
                "menu navigation is not possible\n" +
                "invalid command\n" +
                "user with nickname 2 already exists\n" +
                "nickname changed successfully!\n" +
                "password changed successfully!\n" +
                "invalid command\n" +
                "current password is invalid\n" +
                "invalid command\n" +
                "invalid command\n" +
                "invalid command\n" +
                "current password is invalid\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
    }

    @Test
    void shopTest() {
        MonsterCard card = new SimpleMonster("1", "1", 1, 1, 1, 1, MonsterType.AQUA, MonsterAttributeType.WATER);
        Setuper.setInput("user create -u 1 -p 1 -n 1\n" +
                "user login -u 1 -p 1\n" +
                "menu enter Shop\n" +
                "menu show-current\n" +
                "menu enter Main\n" +
                "wgopwgpw\n" +
                "HESOYAM\n" +
                "TOOLUP\n" +
                "shop buy 1\n" +
                "shop buy Kir\n" +
                "shop buy\n" +
                "menu exit\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
        Assertions.assertEquals("user created successfully!\n" +
                "user logged in successfully!\n" +
                "Shop Menu\n" +
                "menu navigation is not possible\n" +
                "invalid command\n" +
                "$$$$$$$$$$$$$$$$\n" +
                ":)\n" +
                "card bought!\n" +
                "there is no card with this name\n" +
                "invalid command\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
        MonsterCard.getAllMonsterCards().remove(card);
    }

    @Test
    void shopTest2() {
        Setuper.setInput("user create -u 1 -p 1 -n 1\n" +
                "user login -u 1 -p 1\n" +
                "menu enter Shop\n" +
                "menu show-current\n" +
                "menu enter Main\n" +
                "shop show --all\n" +
                "shop buy\n" +
                "menu exit\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
    }

    @Test
    void scoreboardTest() {
        Setuper.setInput("user create -u 1 -p 1 -n 1\n" +
                "user login -u 1 -p 1\n" +
                "menu enter Scoreboard\n" +
                "menu show-current\n" +
                "menu enter Main\n" +
                "wgopwgpw\n" +
                "scoreboard show\n" +
                "menu exit\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
        Assertions.assertEquals("user created successfully!\n" +
                "user logged in successfully!\n" +
                "Scoreboard Menu\n" +
                "menu navigation is not possible\n" +
                "invalid command\n" +
                "1- 1: 0\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
    }
}
