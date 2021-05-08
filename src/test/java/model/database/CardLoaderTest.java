package model.database;

import model.cards.MonsterCard;
import model.cards.SpellCard;
import model.cards.TrapCard;
import model.exceptions.BooAnException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CardLoaderTest {
    @BeforeAll
    static void setup() {
        MonsterCard.getAllMonsterCards().clear();
        SpellCard.getAllSpellCards().clear();
        TrapCard.getAllTrapCards().clear();
    }

    @Test
    void testNormalLoad() {
        CardLoader.loadCards("config/Monster.csv", "config/Spell.csv");
        try {
            CardLoader.loadCards("config/Monster.csv", "config/Spell.csv");
            Assertions.fail("loadCards called twice");
        } catch (BooAnException ignored) {
        }
    }
}
