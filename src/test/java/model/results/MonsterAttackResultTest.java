package model.results;

import model.cards.*;
import model.enums.AttackResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MonsterAttackResultTest {
    private static Card monsterCard;
    @BeforeAll
    static void setup() {
        monsterCard = new SimpleMonster("test", "", 0,0,0,0, MonsterType.AQUA, MonsterAttributeType.DARK);
    }

    @AfterAll
    static void cleanUp() {
        MonsterCard.getAllMonsterCards().remove(monsterCard);
    }

    @Test
    void test() {
        MonsterAttackResult monsterAttackResult = new MonsterAttackResult(50, true, false, monsterCard, AttackResult.ME_DESTROYED);
        Assertions.assertEquals("opponent's monster card was test and no card is destroyed and you received 50 battle damage", monsterAttackResult.toString());
        Assertions.assertEquals(AttackResult.ME_DESTROYED, monsterAttackResult.getBattleResult());
        Assertions.assertEquals(50, monsterAttackResult.getDamageReceived());
        monsterAttackResult = new MonsterAttackResult(50, false, true, monsterCard, AttackResult.ME_DESTROYED);
        Assertions.assertEquals("your monster card is destroyed and you received 50 battle damage", monsterAttackResult.toString());
        monsterAttackResult = new MonsterAttackResult(0, false, false, monsterCard, AttackResult.DRAW);
        Assertions.assertEquals("no card is destroyed", monsterAttackResult.toString());
        monsterAttackResult = new MonsterAttackResult(0, false, true, monsterCard, AttackResult.DRAW);
        Assertions.assertEquals("both you and your opponent monster cards are destroyed and no one receives damage", monsterAttackResult.toString());
        monsterAttackResult = new MonsterAttackResult(0, false, false, monsterCard, AttackResult.RIVAL_DESTROYED);
        Assertions.assertEquals("the defense position monster is destroyed", monsterAttackResult.toString());
        monsterAttackResult = new MonsterAttackResult(50, false, true, monsterCard, AttackResult.RIVAL_DESTROYED);
        Assertions.assertEquals("your opponent's monster is destroyed and your opponent receives 50 battle damage", monsterAttackResult.toString());

    }
}
