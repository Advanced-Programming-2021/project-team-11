package controller.menucontrollers;

import model.User;
import model.cards.Card;
import model.cards.MonsterAttributeType;
import model.cards.MonsterType;
import model.cards.SimpleMonster;
import model.exceptions.CardNotExistsException;
import model.exceptions.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ShopMenuControllerTest {
    @BeforeEach
    void setUp() {
        User.getUsers().clear();
    }

    @Test
    void cheat() {
        User user = new User("1", "1", "1");
        int begin = user.getMoney();
        ShopMenuController.increaseMoneyCheat(user);
        assertEquals(100000, user.getMoney() - begin);
    }

    @Test
    void buy() {
        User user = new User("1", "1", "1");
        int Price = user.getMoney();
        final String cardName = "black nigga";
        new SimpleMonster(cardName, ":|", Price, 0, 0, 0, MonsterType.AQUA, MonsterAttributeType.DARK);
        try {
            ShopMenuController.buyCardForUser(user, cardName);
        } catch (CardNotExistsException | InsufficientBalanceException e) {
            fail(e);
        }
        try {
            ShopMenuController.buyCardForUser(user, cardName + "1");
        } catch (CardNotExistsException ignored) {
        } catch (InsufficientBalanceException e) {
            fail(e);
        }
        try {
            ShopMenuController.buyCardForUser(user, cardName);
        } catch (CardNotExistsException e) {
            fail(e);
        } catch (InsufficientBalanceException ignored) {
        }
    }

    @Test
    void addAll() {
        User user = new User("1", "1", "1");
        ShopMenuController.addAllCardsCheat(user);
        for (Card card : Card.getAllCards())
            if (!user.getAvailableCards().contains(card))
                fail(card.toString() + " does not exists in user deck");
    }
}
