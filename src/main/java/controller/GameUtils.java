package controller;

import model.cards.Card;
import model.cards.monsters.HeraldOfCreation;
import model.cards.monsters.ScannerCard;
import model.cards.traps.CallOfTheHaunted;
import model.cards.traps.TimeSeal;
import model.enums.CoinFlipResult;

import java.util.Arrays;
import java.util.Random;

public class GameUtils {
    public static final Random random = new Random();
    private static final String[] ACTIVATED_TRAP_CARDS = {TimeSeal.getInstance().getName(), CallOfTheHaunted.getInstance().getName()};
    private static final String[] ACTIVATED_MONSTER_CARDS = {ScannerCard.getInstance().getName(), HeraldOfCreation.getInstance().getName()};

    public static CoinFlipResult flipCoin() {
        return random.nextBoolean() ? CoinFlipResult.HEAD : CoinFlipResult.TAIL;
    }

    public static int rollDice() {
        return random.nextInt(6) + 1;
    }

    public static boolean canMonsterCardEffectBeActivated(Card card) {
        // I really don't want to use instance of here :))
        return Arrays.stream(ACTIVATED_MONSTER_CARDS).anyMatch(name -> name.equals(card.getName()));
    }

    public static boolean canTrapCardEffectBeActivated(Card card) {
        // I really don't want to use instance of here :))
        return Arrays.stream(ACTIVATED_TRAP_CARDS).anyMatch(name -> name.equals(card.getName()));
    }
}
