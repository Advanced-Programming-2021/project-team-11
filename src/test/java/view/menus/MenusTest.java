package view.menus;

import model.User;
import model.cards.MonsterAttributeType;
import model.cards.MonsterCard;
import model.cards.MonsterType;
import model.cards.SimpleMonster;
import org.junit.jupiter.api.*;
import view.menus.commands.deck.*;
import view.menus.commands.shop.ShopBuyItemCommand;
import view.menus.commands.user.UserLoginCommand;
import view.menus.commands.user.UserRegisterCommand;

public class MenusTest {
    @BeforeAll
    static void setup() {
        Setuper.setup();
    }

    @AfterAll
    static void cleanUp() {
        User.getUsers().clear();
        Setuper.restore();
    }

    @BeforeEach
    void setUp() {
        User.getUsers().clear();
        Setuper.reset();
    }

    @Test
    void commandTest() {
        Assertions.assertTrue(new UserLoginCommand().isValid());
        Assertions.assertTrue(new UserRegisterCommand().isValid());
        Assertions.assertTrue(new ShopBuyItemCommand().isValid());
        Assertions.assertTrue(new DeckShowCommand().isValid());
        Assertions.assertTrue(new DeckSwapCardCommand().isValid());
        Assertions.assertTrue(new DeckSetActiveCommand().isValid());
        Assertions.assertTrue(new DeckRemoveCardCommand().isValid());
        Assertions.assertTrue(new DeckDeleteCommand().isValid());
        Assertions.assertTrue(new DeckCreateCommand().isValid());
        Assertions.assertTrue(new DeckAddCardCommand().isValid());
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
                "menu enter Main\n" +
                "gewgwehewhewhwe\n" +
                "menu enter Login\n" +
                "menu show-current\n" +
                "logout\n" +
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
                "menu navigation is not possible\n" +
                "invalid command\n" +
                "please use logout command\n" +
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

    @Test
    void importExportTest() {
        MonsterCard card = new SimpleMonster("1", "1", 1, 1, 1, 1, MonsterType.AQUA, MonsterAttributeType.WATER);
        Setuper.setInput("user create -u 1 -p 1 -n 1\n" +
                "user login -u 1 -p 1\n" +
                "menu enter Import/Export\n" +
                "menu show-current\n" +
                "menu enter Main\n" +
                "wgopwgpw\n" +
                "import card ejgwpg\n" +
                "export card ejgwpg\n" +
                "export card 1\n" +
                "import card 1\n" +
                "menu exit\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
        Assertions.assertEquals("user created successfully!\n" +
                "user logged in successfully!\n" +
                "Import/Export Menu\n" +
                "menu navigation is not possible\n" +
                "invalid command\n" +
                "ejgwpg.json\n" +
                "Card does not exists!\n" +
                "Saved in 1.json\n" +
                "1 imported!\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
        MonsterCard.getAllMonsterCards().remove(card);
    }

    @Test
    void deckTest() {
        MonsterCard card = new SimpleMonster("1", "1", 1, 1, 1, 1, MonsterType.AQUA, MonsterAttributeType.WATER);
        Setuper.setInput("user create -u 1 -p 1 -n 1\n" +
                "user login -u 1 -p 1\n" +
                "menu enter Shop\n" +
                "shop buy 1\n" +
                "menu exit\n" +
                "menu enter Deck\n" +
                "menu show-current\n" +
                "menu enter Main\n" +
                "wgopwgpw\n" +
                "deck create 1\n" +
                "deck create 1\n" +
                "deck add-card --card 1 -d 1 -s\n" +
                "deck add-card --card 1 -d 1 -s\n" +
                "deck rm-card -c 1 -d 1\n" +
                "deck rm-card -c 1 -d 1 -s\n" +
                "deck show -n 2 -s\n" +
                "deck show -n 1 -s\n" +
                "deck show -n 1\n" +
                "deck show --all\n" +
                "deck delete 2\n" +
                "deck set-activate 1\n" +
                "deck show --all\n" +
                "deck delete 1\n" +
                "deck set-activate 1\n" +
                "deck show --cards\n" +
                "deck show --all\n" +
                "DECKUP\n" +
                "deck set-activate deckup-deck\n" +
                "menu exit\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
        Assertions.assertEquals("user created successfully!\n" +
                "user logged in successfully!\n" +
                "card bought!\n" +
                "Deck Menu\n" +
                "menu navigation is not possible\n" +
                "invalid command\n" +
                "deck created successfully!\n" +
                "deck with name 1 already exists\n" +
                "card added to deck successfully\n" +
                "card with name 1 does not exist\n" +
                "card with name 1 does not exist in main deck\n" +
                "card removed form deck successfully\n" +
                "deck with name 2 does not exist\n" +
                "Deck: 1\n" +
                "Side deck:\n" +
                "Monsters:\n" +
                "Spell and Traps:\n" +
                "Deck: 1\n" +
                "Main deck:\n" +
                "Monsters:\n" +
                "Spell and Traps:\n" +
                "Decks:\n" +
                "Active deck:\n" +
                "Other decks:\n" +
                "1: main deck 0, side deck 0, invalid\n" +
                "deck with name 2 does not exist\n" +
                "deck activated successfully\n" +
                "Decks:\n" +
                "Active deck:\n" +
                "1: main deck 0, side deck 0, invalid\n" +
                "Other decks:\n" +
                "deck deleted successfully\n" +
                "deck with name 1 does not exist\n" +
                "1:1\n" +
                "Decks:\n" +
                "Active deck:\n" +
                "Other decks:\n" +
                "Added a deck with name of deckup-deck :D\n" +
                "deck activated successfully\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
        MonsterCard.getAllMonsterCards().remove(card);
    }

    @Test
    void testDuelStartMenu() {
        Setuper.setInput("user create -u 1 -p 1 -n 1\n" +
                "user create -u 2 -p 2 -n 2\n" +
                "user login -u 1 -p 1\n" +
                "menu enter Duel\n" +
                "kir\n" +
                "menu show-current\n" +
                "menu enter Main\n" +
                "duel -n -r 1\n" +
                "duel -n -r 2 -s 3\n" +
                "duel -n -r 3 -s 3\n" +
                "duel -n -r 3 -s 1\n" +
                "duel -n -r 3 --ai\n" +
                "duel -n -r 3 -s 2\n" +
                "menu exit\n" +
                "menu exit\n" +
                "menu exit\n");
        new LoginMenu();
        Assertions.assertEquals("user created successfully!\n" +
                "user created successfully!\n" +
                "user logged in successfully!\n" +
                "invalid command\n" +
                "Duel Menu\n" +
                "menu navigation is not possible\n" +
                "invalid command\n" +
                "number of rounds is not supported\n" +
                "there is no player with this username\n" +
                "You have played yourself!\n" +
                "not this time...\n" +
                "1 has no active deck\n" +
                "user logged out successfully!\n", Setuper.getOutputString());
    }
}
