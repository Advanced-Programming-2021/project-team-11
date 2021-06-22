package model.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameEndResultsTest {
    @Test
    void test1() {
        GameEndResults results = new GameEndResults(50, 100, true, 1);
        Assertions.assertTrue(results.didPlayer1Won());
        Assertions.assertEquals(1050, results.getPlayer1Money());
        Assertions.assertEquals(100, results.getPlayer2Money());
        Assertions.assertEquals(1000, results.getPlayer1Score());
        Assertions.assertEquals(0, results.getPlayer2Score());
    }

    @Test
    void test2() {
        GameEndResults results = new GameEndResults(50, 100, false, 3);
        Assertions.assertFalse(results.didPlayer1Won());
        Assertions.assertEquals(300, results.getPlayer1Money());
        Assertions.assertEquals(3300, results.getPlayer2Money());
        Assertions.assertEquals(0, results.getPlayer1Score());
        Assertions.assertEquals(3000, results.getPlayer2Score());
    }
}
