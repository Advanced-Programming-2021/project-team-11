package model.database;

import model.cards.MonsterCard;
import model.cards.SpellCard;
import model.cards.TrapCard;
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
        MonsterCard.getAllMonsterCards().clear();
        SpellCard.getAllSpellCards().clear();
        TrapCard.getAllTrapCards().clear();
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
        Assertions.assertEquals(lines, MonsterCard.getAllMonsterCards().size());
    }
}
