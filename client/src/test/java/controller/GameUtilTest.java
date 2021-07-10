package controller;

import model.cards.monsters.HeraldOfCreation;
import model.cards.monsters.ScannerCard;
import model.cards.traps.CallOfTheHaunted;
import model.cards.traps.TimeSeal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameUtilTest {
    @Test
    void testRng() {
        GameUtils.flipCoin();
        GameUtils.rollDice();
    }

    @Test
    void testCards() {
        Assertions.assertTrue(GameUtils.canMonsterCardEffectBeActivated(ScannerCard.getInstance()));
        Assertions.assertTrue(GameUtils.canMonsterCardEffectBeActivated(HeraldOfCreation.getInstance()));
        Assertions.assertFalse(GameUtils.canTrapCardEffectBeActivated(HeraldOfCreation.getInstance()));
        Assertions.assertFalse(GameUtils.canTrapCardEffectBeActivated(ScannerCard.getInstance()));
        Assertions.assertTrue(GameUtils.canTrapCardEffectBeActivated(TimeSeal.getInstance()));
        Assertions.assertTrue(GameUtils.canTrapCardEffectBeActivated(CallOfTheHaunted.getInstance()));
    }
}
