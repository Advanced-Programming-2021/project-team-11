package controller;

import model.cards.Card;
import model.cards.monsters.ScannerCard;
import model.enums.CoinFlipResult;

import java.util.Arrays;
import java.util.Random;

public class GameUtils {
    public static final Random random = new Random();

    public static CoinFlipResult flipCoin() {
        return random.nextBoolean() ? CoinFlipResult.HEAD : CoinFlipResult.TAIL;
    }

    public static int rollDice() {
        return random.nextInt(6) + 1;
    }

    public static boolean canMonsterCardEffectBeActivated(Card card) {
        // I really don't want to use instance of here
        String[] okCards = {ScannerCard.getCardName()};
        return Arrays.stream(okCards).anyMatch(name -> name.equals(card.getName()));
    }
}
