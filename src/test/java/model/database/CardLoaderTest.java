package model.database;

import model.cards.*;
import model.exceptions.BooAnException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CardLoaderTest {
    private static final String monsterCardsLocation = "config/Monster.csv";
    private static final String spellCardsLocation = "config/Spell.csv";

    @BeforeAll
    static void setup() {
        MonsterCard.getAllMonsterCards().removeIf(x -> x instanceof SimpleMonster || x instanceof RitualMonster);
        CardLoader.loadCards(monsterCardsLocation, spellCardsLocation);
    }

    @Test
    void testDoubleLoad() {
        try {
            CardLoader.loadCards(monsterCardsLocation, spellCardsLocation);
            Assertions.fail("loadCards called twice");
        } catch (BooAnException ignored) {
        }
    }

    @Test
    void countMonsters() {
        int lines = -1;
        try {
            lines = Files.readAllLines(Paths.get(monsterCardsLocation)).size() - 1;
        } catch (IOException e) {
            Assertions.fail(e);
        }
        Assertions.assertEquals(lines, MonsterCard.getAllMonsterCards().stream().filter(Card::isInitialized).count());
    }
}
