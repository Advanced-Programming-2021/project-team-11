package model.database;

import model.Deck;
import model.User;
import model.cards.monsters.BeastKingBarbaros;
import model.cards.monsters.ScannerCard;
import model.cards.monsters.YomiShip;
import model.cards.spells.AdvancedRitualArt;
import model.cards.spells.ClosedForest;
import model.cards.spells.DarkHole;
import model.cards.spells.Yami;
import model.cards.traps.CallOfTheHaunted;
import model.cards.traps.TimeSeal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class UsersDatabaseTest {
    @BeforeAll
    static void setup() {
        CardLoaderTest.setupCards();
        createUsers();
    }

    @AfterAll
    static void removeUsers() {
        User.getUsers().clear();
    }

    private static void createUsers() {
        User.getUsers().clear();
        User user1 = new User("1", "1", "1");
        User user2 = new User("2", "2", "2");
        {
            Deck deck = new Deck();
            deck.addCardToSideDeck(YomiShip.getInstance());
            deck.addCardToSideDeck(Yami.getInstance());
            deck.addCardToMainDeck(BeastKingBarbaros.getInstance());
            deck.addCardToMainDeck(TimeSeal.getInstance());
            user1.addDeck("deck1", deck);
        }
        {
            Deck deck = new Deck();
            deck.addCardToSideDeck(CallOfTheHaunted.getInstance());
            user1.addDeck("deck2", deck);
            user1.setActiveDeck("deck2");
        }
        user1.addCardToPlayer(ScannerCard.getInstance());
        {
            Deck deck = new Deck();
            deck.addCardToSideDeck(AdvancedRitualArt.getInstance());
            deck.addCardToSideDeck(ClosedForest.getInstance());
            deck.addCardToMainDeck(DarkHole.getInstance());
            deck.addCardToMainDeck(TimeSeal.getInstance());
            user2.addDeck("deck1", deck);
        }
    }

    @Test
    void databaseTest() {
        try {
            UsersDatabase.connectToDatabase("test.db");
            UsersDatabase.saveUsers();
        } catch (SQLException ex) {
            Assertions.fail(ex);
        }
        User.getUsers().clear();
        try {
            UsersDatabase.loadUsers();
            UsersDatabase.closeDatabase();
        } catch (SQLException ex) {
            Assertions.fail(ex);
        }
        // check the data
        User user1 = User.getUserByUsername("1");
        User user2 = User.getUserByUsername("2");
        Assertions.assertNotNull(user1);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals("deck2", user1.getActiveDeckName());
        Assertions.assertNotNull(user1.getDeckByName("deck1"));
        Assertions.assertTrue(user1.getActiveDeck().getSideDeck().stream().anyMatch(x -> x instanceof CallOfTheHaunted));
        Assertions.assertNotNull(user2.getDeckByName("deck1"));
        Assertions.assertNull(user2.getActiveDeck());
        Assertions.assertEquals(1, user1.getAvailableCards().size());
        Assertions.assertTrue(user1.getAvailableCards().get(0) instanceof ScannerCard);
    }
}
