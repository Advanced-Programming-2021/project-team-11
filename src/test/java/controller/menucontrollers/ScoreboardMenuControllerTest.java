package controller.menucontrollers;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreboardMenuControllerTest {
    @BeforeEach
    void setUp() {
        User.getUsers().clear();
        new User("hovakhshatara:", "2", "hovakhshatara").increaseScore(4000);
        new User("ebrahim_1379", "3", "ebrahim_1379").increaseScore(3000);
        new User("the-ultimate-mahD", "4", "the-ultimate-mahD").increaseScore(3000);
        new User("shahin", "1", "shahin").increaseScore(5000);
        new User("rostam-dastan", "5", "rostam-dastan").increaseScore(1000);
    }

    @Test
    void getScoreboard() {
        ArrayList<String> scoreboard = ScoreboardMenuController.getScoreboardLines();
        assertEquals("1- shahin: 5000", scoreboard.get(0));
        assertEquals("2- hovakhshatara: 4000", scoreboard.get(1));
        assertEquals("3- ebrahim_1379: 3000", scoreboard.get(2));
        assertEquals("3- the-ultimate-mahD: 3000", scoreboard.get(3));
        assertEquals("5- rostam-dastan: 1000", scoreboard.get(4));
    }
}
