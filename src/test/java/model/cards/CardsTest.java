package model.cards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CardsTest {
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
}
