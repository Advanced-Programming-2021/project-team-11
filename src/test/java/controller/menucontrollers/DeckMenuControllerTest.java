package controller.menucontrollers;

import model.User;
import model.cards.monsters.TerratigerTheEmpoweredWarrior;
import model.cards.monsters.YomiShip;
import model.cards.spells.MonsterReborn;
import model.cards.spells.Yami;
import model.cards.traps.TimeSeal;
import model.database.CardLoaderTest;
import model.exceptions.*;
import model.results.GetDecksResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class DeckMenuControllerTest {
    private static User user;
    private static final String deckUpDeckName = "deckup-deck";

    @BeforeAll
    static void setup() {
        CardLoaderTest.setupCards();
        user = new User("1", "1", "1");
    }

    @AfterAll
    static void cleanup() {
        User.getUsers().remove(user);
        user = null;
    }

    @Test
    void deckupTest() {
        for (int i = 0; i < 100; i++) // Should not throw any errors
            DeckMenuController.addAllCardsToNewDeck(user);
    }

    @Test
    void addDeckTest() {
        final String deckName = "kire khar";
        try {
            DeckMenuController.addDeck(user, deckName);
        } catch (DeckExistsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.addDeck(user, deckName);
            Assertions.fail("deck added twice");
        } catch (DeckExistsException ignore) {
        }
    }

    @Test
    void deleteDeckTest() {
        DeckMenuController.addAllCardsToNewDeck(user);
        try {
            DeckMenuController.deleteDeck(user, deckUpDeckName);
        } catch (DeckDoesNotExistsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.deleteDeck(user, "whehw");
            Assertions.fail("invalid deck deleted");
        } catch (DeckDoesNotExistsException ignored) {
        }
    }

    @Test
    void activeDeckTest() {
        DeckMenuController.addAllCardsToNewDeck(user);
        try {
            DeckMenuController.setActiveDeck(user, deckUpDeckName);
        } catch (DeckDoesNotExistsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.setActiveDeck(user, "gjeiowgewge");
            Assertions.fail("unknown deck activated");
        } catch (DeckDoesNotExistsException ignored) {
        }
        try {
            DeckMenuController.deleteDeck(user, deckUpDeckName);
        } catch (DeckDoesNotExistsException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void addCardTest() {
        final String deckName = "wkofpwpgkw";
        for (int i = 0; i < 100; i++) {
            user.addCardToPlayer(Yami.getInstance());
            user.addCardToPlayer(TerratigerTheEmpoweredWarrior.getInstance());
        }
        try {
            DeckMenuController.addDeck(user, deckName);
        } catch (DeckExistsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.addCardToDeck(user, deckName, Yami.getInstance().getName(), true);
        } catch (DeckDoesNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.addCardToDeck(user, deckName, "wjegiojewoigjoiewjiogjoiowieg", true);
            Assertions.fail("card added");
        } catch (DeckDoesNotExistsException | DeckSideOrMainFullException | DeckHaveThreeCardsException e) {
            Assertions.fail(e);
        } catch (CardNotExistsException ignored) {
        }
        try {
            DeckMenuController.addCardToDeck(user, "ewgioepgwpo", Yami.getInstance().getName(), true);
            Assertions.fail("card added");
        } catch (DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            Assertions.fail(e);
        } catch (DeckDoesNotExistsException ignored) {
        }
        try {
            DeckMenuController.addCardToDeck(user, deckName, TerratigerTheEmpoweredWarrior.getInstance().getName(), true);
            DeckMenuController.addCardToDeck(user, deckName, TerratigerTheEmpoweredWarrior.getInstance().getName(), false);
            DeckMenuController.addCardToDeck(user, deckName, TerratigerTheEmpoweredWarrior.getInstance().getName(), true);
        } catch (DeckDoesNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.addCardToDeck(user, deckName, TerratigerTheEmpoweredWarrior.getInstance().getName(), true);
            Assertions.fail("card added");
        } catch (DeckDoesNotExistsException | DeckSideOrMainFullException | CardNotExistsException e) {
            Assertions.fail(e);
        } catch (DeckHaveThreeCardsException ignored) {
        }
    }

    @Test
    void removeCardTest() {
        user.getAvailableCards().clear();
        final String deckName = "gwqkgwq[gew";
        user.addCardToPlayer(Yami.getInstance());
        try {
            DeckMenuController.addDeck(user, deckName);
        } catch (DeckExistsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.addCardToDeck(user, deckName, Yami.getInstance().getName(), false);
        } catch (DeckDoesNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            Assertions.fail(e);
        }
        Assertions.assertEquals(0, user.getAvailableCards().size());
        try {
            DeckMenuController.removeCardFromDeck(user, deckName, Yami.getInstance().getName(), true);
            Assertions.fail("card removed");
        } catch (DeckDoesNotExistsException e) {
            Assertions.fail(e);
        } catch (DeckCardNotExistsException ignored) {
        }
        try {
            DeckMenuController.removeCardFromDeck(user, deckName, Yami.getInstance().getName(), false);
        } catch (DeckDoesNotExistsException | DeckCardNotExistsException e) {
            Assertions.fail(e);
        }
        Assertions.assertEquals(1, user.getAvailableCards().size());
        try {
            DeckMenuController.removeCardFromDeck(user, "wkfogpjkwiogiejoige", Yami.getInstance().getName(), false);
        } catch (DeckDoesNotExistsException ignored) {
        } catch (DeckCardNotExistsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.addCardToDeck(user, deckName, Yami.getInstance().getName(), true);
        } catch (DeckDoesNotExistsException | DeckSideOrMainFullException | CardNotExistsException | DeckHaveThreeCardsException e) {
            Assertions.fail(e);
        }
        try {
            DeckMenuController.removeCardFromDeck(user, deckName, Yami.getInstance().getName(), true);
        } catch (DeckDoesNotExistsException | DeckCardNotExistsException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getDecksTest() {
        deckupTest(); // deckup
        GetDecksResult result = DeckMenuController.getDecks(user);
        Assertions.assertNull(result.getActiveDeck());
        Optional<GetDecksResult.DeckResult> deck = result.getOtherDecks().stream().filter(x -> x.getName().equals(deckUpDeckName)).findFirst();
        if (!deck.isPresent())
            Assertions.fail("deck does not exists in result");
        Assertions.assertEquals("deckup-deck: main deck 60, side deck 15, valid", deck.get().toString());
    }

    @Test
    void getDeckCardsTest() {
        deckupTest(); // deckup
        try {
            Assertions.assertEquals(15, DeckMenuController.getDeckCards(user, deckUpDeckName, true).size());
            Assertions.assertEquals(60, DeckMenuController.getDeckCards(user, deckUpDeckName, false).size());
        } catch (DeckDoesNotExistsException ex) {
            Assertions.fail(ex);
        }
        try {
            DeckMenuController.getDeckCards(user, "wgwgwgw", false);
            Assertions.fail("cards got :|");
        } catch (DeckDoesNotExistsException ignored) {
        }
    }

    @Test
    void getCardsTest() {
        final String deckName = "wkgwgwgweghw";
        user.getDecks().clear();
        user.getAvailableCards().clear();
        user.addCardToPlayer(YomiShip.getInstance());
        user.addCardToPlayer(MonsterReborn.getInstance());
        user.addCardToPlayer(TimeSeal.getInstance());
        try {
            DeckMenuController.addDeck(user, deckName);
            DeckMenuController.addCardToDeck(user, deckName, YomiShip.getInstance().getName(), true);
            DeckMenuController.addCardToDeck(user, deckName, MonsterReborn.getInstance().getName(), false);
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
        Assertions.assertEquals(3, DeckMenuController.getAllCards(user).size());
    }

    @Test
    void swapCardsTest() {
        final String deckName = "jetjtjh";
        user.getDecks().clear();
        user.getAvailableCards().clear();
        user.addCardToPlayer(YomiShip.getInstance());
        user.addCardToPlayer(MonsterReborn.getInstance());
        try {
            DeckMenuController.addDeck(user, deckName);
            DeckMenuController.addCardToDeck(user, deckName, YomiShip.getInstance().getName(), true);
            DeckMenuController.addCardToDeck(user, deckName, MonsterReborn.getInstance().getName(), false);
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
        try {
            DeckMenuController.swapCards(user.getDeckByName(deckName), YomiShip.getInstance().getName(), YomiShip.getInstance().getName());
            Assertions.fail("cards swapped");
        } catch (DeckCardNotExistsException ignored) {
        }
        try {
            DeckMenuController.swapCards(user.getDeckByName(deckName), MonsterReborn.getInstance().getName(), MonsterReborn.getInstance().getName());
            Assertions.fail("cards swapped");
        } catch (DeckCardNotExistsException ignored) {
        }
        try {
            DeckMenuController.swapCards(user.getDeckByName(deckName), MonsterReborn.getInstance().getName(), YomiShip.getInstance().getName());
        } catch (DeckCardNotExistsException ex) {
            Assertions.fail(ex);
        }
    }
}
