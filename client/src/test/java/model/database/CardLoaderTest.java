package model.database;

import model.exceptions.BooAnException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CardLoaderTest {
    private static final String monsterCardsLocation = "config/Monster.csv";
    private static final String spellCardsLocation = "config/Spell.csv";

    public static void setupCards() {
        try {
            CardLoader.loadCards(monsterCardsLocation, spellCardsLocation);
        } catch (Exception ex) {
            // cards might be loaded from another function
        }
    }

    @BeforeAll
    public static void setup() {
        setupCards();
    }

    @Test
    void testDoubleLoad() {
        try {
            CardLoader.loadCards(monsterCardsLocation, spellCardsLocation);
            Assertions.fail("loadCards called twice");
        } catch (BooAnException ignored) {
        }
    }
}
