package view.menus;

import model.Player;
import model.PlayerBoard;
import model.User;
import model.cards.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DuelMenuTest {

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
        assertEquals(2, DuelMenuUtils.inputToRivalBoard(1));
        assertEquals(3, DuelMenuUtils.inputToRivalBoard(2));
        assertEquals(1, DuelMenuUtils.inputToRivalBoard(3));
        assertEquals(4, DuelMenuUtils.inputToRivalBoard(4));
        assertEquals(0, DuelMenuUtils.inputToRivalBoard(5));
    }
}
