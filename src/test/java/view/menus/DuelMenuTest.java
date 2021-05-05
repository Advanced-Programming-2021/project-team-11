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

    @BeforeEach
    void setUp() {
        User.getUsers().clear();
        MonsterCard.getAllMonsterCards().clear();
        SpellCard.getAllSpellCards().clear();
        TrapCard.getAllTrapCards().clear();
        Setuper.reset();
    }

    @AfterAll
    static void cleanUp() {
        User.getUsers().clear();
        MonsterCard.getAllMonsterCards().clear();
        SpellCard.getAllSpellCards().clear();
        TrapCard.getAllTrapCards().clear();
        Setuper.restore();
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
        DuelMenu.printRivalBoard(board);
        assertEquals("\tc\tc\tc\tc\tc\n" +
                "35\n" +
                "\tE \tE \tE \tE \tE \n" +
                "\tE \tE \tE \tE \tE \n" +
                "00\t\t\t\t\t\tE\n", Setuper.getOutput().toString());
        Setuper.reset();
        DuelMenu.printMyBoard(board);
        assertEquals("00\t\t\t\t\t\tE\n" +
                "\tE \tE \tE \tE \tE \n" +
                "\tE \tE \tE \tE \tE \n" +
                "\t\t\t\t\t\t35\n" +
                "c\tc\tc\tc\tc\t\n", Setuper.getOutput().toString());
    }
}