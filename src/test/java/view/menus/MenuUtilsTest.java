package view.menus;

import org.junit.jupiter.api.*;

public class MenuUtilsTest {
    @BeforeAll
    static void setup() {
        Setuper.setup();
    }

    @BeforeEach
    void setUp() {
        Setuper.reset();
    }

    @AfterAll
    static void cleanUp() {
        Setuper.restore();
    }

    @Test
    void showCardTest() {
        Assertions.assertFalse(MenuUtils.showCard("card show"));
        Assertions.assertTrue(MenuUtils.showCard("card show kire khar bozorg"));
        Assertions.assertEquals("Card does not exists\n", Setuper.getOutputString());
        Assertions.assertTrue(MenuUtils.showCard("card show Battle OX"));
    }

    @Test
    void selectCardTest() {
        Setuper.setInput("10\n:|\n-1\n0\n1\n");
        Assertions.assertEquals(0, MenuUtils.readCardByIndex(5));
        Setuper.setInput("cancel\n");
        Assertions.assertEquals(-1, MenuUtils.readCardByIndex(5));
    }
}
