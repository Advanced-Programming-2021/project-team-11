package controller.menucontrollers;

import model.User;
import model.cards.Card;
import model.exceptions.CardNotExistsException;
import model.exceptions.InsufficientBalanceException;

public class ShopMenuController {
    private static final int INCREASE_MONEY_CHEAT_DIFF = 100000;
    public static void buyCardForUser(User user, String cardName) throws CardNotExistsException, InsufficientBalanceException {
        Card card = Card.getCardByName(cardName);
        if (card == null)
            throw new CardNotExistsException("there is no card with this name", cardName);
        if (user.getMoney() < card.getPrice())
            throw new InsufficientBalanceException();
        user.decreaseMoney(card.getPrice());
        user.getAvailableCards().add(card);
    }

    public static void increaseMoneyCheat(User user) {
        user.increaseMoney(INCREASE_MONEY_CHEAT_DIFF);
    }
}
