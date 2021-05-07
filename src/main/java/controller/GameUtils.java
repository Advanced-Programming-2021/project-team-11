package controller;

import model.enums.CoinFlipResult;

import java.util.Random;

public class GameUtils {
    public static final Random random = new Random();

    public static CoinFlipResult flipCoin() {
        return random.nextBoolean() ? CoinFlipResult.HEAD : CoinFlipResult.TAIL;
    }

    public static int rollDice() {
        return random.nextInt(6) + 1;
    }
}
