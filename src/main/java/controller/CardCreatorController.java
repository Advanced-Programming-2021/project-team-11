package controller;

import model.cards.MonsterCard;

import java.util.Objects;
import java.util.Random;

public class CardCreatorController {
    private static final int ROUND_OFF = 100;

    /**
     * Predicts the price of a monster in card creator
     *
     * @param attack   Attack points
     * @param defence  Defence points
     * @param level    Level of card
     * @param isRitual Is this card a ritual monster?
     * @return The price of the card. Returns -1 if anything goes wrong
     */
    public static int predictPrice(String attack, String defence, int level, boolean isRitual) {
        // At first create a semi-random offset based on values give. This offset is constant for same inputs
        int offset = new Random(Objects.hash(attack, defence, level, isRitual)).nextInt(500) - 250;
        // Convert attack and defence to int
        int attackInt, defenceInt;
        try {
            attackInt = Integer.parseInt(attack);
            defenceInt = Integer.parseInt(defence);
        } catch (NumberFormatException ex) {
            return -1;
        }
        if (!isAttackDefenceValid(attackInt) || !isAttackDefenceValid(defenceInt))
            return -1;
        // Calculate the price with a shitty formula
        double price = 400;
        price += attackInt * 1.1 + defenceInt;
        price -= MonsterCard.getCardsNeededToTribute(level) * 100;
        if (isRitual)
            price -= 200;
        int intPrice = (int) price;
        if (intPrice + offset <= 0)
            return roundOff(intPrice);
        return roundOff(intPrice + offset);
    }

    private static int roundOff(int a) {
        return a - (a % ROUND_OFF);
    }

    private static boolean isAttackDefenceValid(int points) {
        return points >= 0 && points <= 4000;
    }
}
