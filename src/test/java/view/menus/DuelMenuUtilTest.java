package view.menus;

import model.PlayableCard;
import model.Player;
import model.PlayerBoard;
import model.User;
import model.cards.*;
import model.enums.CardPlaceType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DuelMenuUtilTest {

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
    void printRivalBoard() {
        SimpleMonster monster = new SimpleMonster("black nigga", ":|", 100, 1, 100, 100, MonsterType.WARRIOR, MonsterAttributeType.DARK);
        User user = new User("1", "1", "1");
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < 40; i++)
            cards.add(monster);
        PlayerBoard board = new PlayerBoard(new Player(user), cards);
        // Redirect output
        DuelMenuUtils.printRivalBoard(board);
        assertEquals("\tc\tc\tc\tc\tc\n" +
                "35\n" +
                "\tE \tE \tE \tE \tE \n" +
                "\tE \tE \tE \tE \tE \n" +
                "00\t\t\t\t\t\tE\n", Setuper.getOutputString());
        Setuper.reset();
        DuelMenuUtils.printMyBoard(board);
        assertEquals("00\t\t\t\t\t\tE\n" +
                "\tE \tE \tE \tE \tE \n" +
                "\tE \tE \tE \tE \tE \n" +
                "\t\t\t\t\t\t35\n" +
                "c\tc\tc\tc\tc\t\n", Setuper.getOutputString());
        MonsterCard.getAllMonsterCards().remove(monster);
    }

    @Test
    void testSelection() {
        assertEquals(2, DuelMenuUtils.inputToPlayerBoard(1));
        assertEquals(1, DuelMenuUtils.inputToPlayerBoard(2));
        assertEquals(3, DuelMenuUtils.inputToPlayerBoard(3));
        assertEquals(0, DuelMenuUtils.inputToPlayerBoard(4));
        assertEquals(4, DuelMenuUtils.inputToPlayerBoard(5));
    }

    @Test
    void testPrintGraveYard() {
        User user = new User("1", "1", "1");
        MonsterCard monsterCard = new SimpleMonster("test", "", 0, 1, 1000, 1000, MonsterType.AQUA, MonsterAttributeType.WATER);
        DuelMenuUtils.printGraveyard(new ArrayList<>(Collections.singletonList(new PlayableCard(monsterCard, CardPlaceType.GRAVEYARD))), user);
        DuelMenuUtils.printGraveyard(new ArrayList<>(), user);
        Assertions.assertEquals("1's graveyard\n" +
                "1. test:\n" +
                "1's graveyard\n" +
                "graveyard empty\n", Setuper.getOutputString());
        User.getUsers().clear();
        MonsterCard.getAllMonsterCards().remove(monsterCard);
    }

    @Test
    void testReadCardsToTribute() {
        Setuper.setInput("a\n" +
                "3\n" +
                "444\n" +
                "2\n" +
                "3\n" +
                "-1\n" +
                "0\n" +
                "1\n" +
                "cancel\n");
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), DuelMenuUtils.readCardsToTribute(3));
        Assertions.assertEquals("Select a card position to tribute or type \"cancel\" to cancel (0/3): Select a card position to tribute or type \"cancel\" to cancel (0/3): Select a card position to tribute or type \"cancel\" to cancel (1/3): Select a card position to tribute or type \"cancel\" to cancel (1/3): Select a card position to tribute or type \"cancel\" to cancel (2/3): Select a card position to tribute or type \"cancel\" to cancel (2/3): Select a card position to tribute or type \"cancel\" to cancel (2/3): Select a card position to tribute or type \"cancel\" to cancel (2/3): ", Setuper.getOutputString());
        Assertions.assertNull(DuelMenuUtils.readCardsToTribute(3));
    }

    @Test
    void testPrintCards() {
        SimpleMonster monster = new SimpleMonster("black nigga", ":|", 100, 1, 100, 100, MonsterType.WARRIOR, MonsterAttributeType.DARK);
        DuelMenuUtils.printNumberedRawCardList(Arrays.stream(new Card[]{monster}));
        DuelMenuUtils.printNumberedCardList(Arrays.stream(new PlayableCard[]{new PlayableCard(monster, CardPlaceType.HAND)}));
        DuelMenuUtils.printNumberedCardListWithLevel(new ArrayList<>(Collections.singletonList(new PlayableCard(monster, CardPlaceType.HAND))));
        Assertions.assertEquals("1. black nigga\n" +
                "1. black nigga\n" +
                "1. black nigga -> level 1\n", Setuper.getOutputString());
        MonsterCard.getAllMonsterCards().remove(monster);
    }

    @Test
    void testPrintAndGetListOfCardToChooseWithCancel() {
        Setuper.setInput("1\n3\n");
        Assertions.assertEquals(0, DuelMenuUtils.printAndGetListOfCardToChooseWithCancel(new String[]{"1", "2"}));
        Assertions.assertEquals(-1, DuelMenuUtils.printAndGetListOfCardToChooseWithCancel(new String[]{"1", "2"}));
        Assertions.assertEquals("Select a card:\n" +
                "1. 1\n" +
                "2. 2\n" +
                "3. cancel the activation\n" +
                "Choose a card by it's index: Select a card:\n" +
                "1. 1\n" +
                "2. 2\n" +
                "3. cancel the activation\n" +
                "Choose a card by it's index: ", Setuper.getOutputString());
    }
}
