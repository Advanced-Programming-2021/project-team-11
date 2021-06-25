package model.cards;

import model.cards.monsters.BeastKingBarbaros;
import model.database.CardLoaderTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CardsTest {
    @BeforeAll
    static void setup() {
        CardLoaderTest.setupCards();
    }

    @Test
    void testTributes() {
        ArrayList<Integer> tributesNeeded = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            MonsterCard monsterCard = new SimpleMonster("test", "", 0, i, 1000, 1000, MonsterType.AQUA, MonsterAttributeType.WATER);
            tributesNeeded.add(monsterCard.getCardsNeededToTribute());
            MonsterCard.getAllMonsterCards().remove(monsterCard);
        }
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 2, 2, 3)), tributesNeeded);
    }

    @Test
    void simpleMonsterTest() {
        MonsterCard monsterCard = new SimpleMonster("test", "", 0, 1, 1000, 1000, MonsterType.AQUA, MonsterAttributeType.WATER);
        Assertions.assertFalse(monsterCard.isConditionMade(null, null, null, 0));
        monsterCard.activateEffect(null, null, null, null, 0);
        monsterCard.deactivateEffect();
        MonsterCard.getAllMonsterCards().remove(monsterCard);
        monsterCard = new RitualMonster("test", "", 0, 1, 1000, 1000, MonsterType.AQUA, MonsterAttributeType.WATER);
        Assertions.assertFalse(monsterCard.isConditionMade(null, null, null, 0));
        monsterCard.activateEffect(null, null, null, null, 0);
        monsterCard.deactivateEffect();
        MonsterCard.getAllMonsterCards().remove(monsterCard);
    }

    @Test
    void testSpecialMonsters() {
        BeastKingBarbaros.getInstance().activateEffect(null, null, null, null, 0);
        Assertions.assertFalse(BeastKingBarbaros.getInstance().isConditionMade(null, null, null, 0));
        // FUCK IT I DONT HAVE TIME :|
    }
}
