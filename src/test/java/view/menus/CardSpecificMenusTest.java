package view.menus;

import model.PlayerBoard;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardSpecificMenusTest {
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
    void testHandleManEaterBugRemoval() {
        //CardSpecificMenus.handleManEaterBugRemoval(new PlayerBoard());
    }
}
