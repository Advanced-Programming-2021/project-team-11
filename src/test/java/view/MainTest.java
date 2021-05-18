package view;

import model.User;
import org.junit.jupiter.api.*;
import view.menus.Setuper;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
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
    void simpleTest() {
        try {
            Main.main(new String[]{"--database", "test.db", "--monster"});
            fail("read cards from the thin air :|");
        } catch (Exception ignored) {
        }
        Setuper.setInput("menu exit\n");
        try {
            Main.main(new String[]{"--database", "test.db", "--monster", "config/monster.csv", "--spell", "config/spell.csv"});
        } catch (Exception ex) {
            fail(ex);
        }
    }
}