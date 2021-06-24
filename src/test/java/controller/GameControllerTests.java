package controller;

import model.Deck;
import model.User;
import model.cards.MonsterAttributeType;
import model.cards.MonsterCard;
import model.cards.MonsterType;
import model.cards.SimpleMonster;
import model.database.CardLoaderTest;
import model.enums.CardPlaceType;
import model.enums.GamePhase;
import model.enums.GameRounds;
import model.enums.GameStatus;
import model.exceptions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GameControllerTests {
    private static User user1, user2;
    private static MonsterCard monsterCardStrong, monsterCardWeek, monsterCardTribute;
    private static final String strongCardName = "strong", weekCardName = "week", tributeCardName = "tribute";

    @BeforeAll
    static void setup() {
        CardLoaderTest.setupCards();
        monsterCardStrong = new SimpleMonster(strongCardName, "", 0, 1, 1000, 1000, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardWeek = new SimpleMonster(weekCardName, "", 0, 1, 100, 100, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardTribute = new SimpleMonster(tributeCardName, "", 0, 5, 2000, 2000, MonsterType.AQUA, MonsterAttributeType.WATER);
        user1 = new User("1", "1", "1");
        {
            Deck deck = new Deck();
            for (int i = 0; i < 10; i++)
                deck.addCardToMainDeck(monsterCardStrong);
            user1.addDeck("1", deck);
            user1.setActiveDeck("1");
        }
        user2 = new User("1", "1", "1");
        {
            Deck deck = new Deck();
            for (int i = 0; i < 10; i++)
                deck.addCardToMainDeck(monsterCardStrong);
            user2.addDeck("1", deck);
            user2.setActiveDeck("1");
        }
    }

    @AfterAll
    static void cleanup() {
        MonsterCard.getAllMonsterCards().remove(monsterCardStrong);
        MonsterCard.getAllMonsterCards().remove(monsterCardWeek);
        MonsterCard.getAllMonsterCards().remove(monsterCardTribute);
        User.getUsers().clear();
    }

    @Test
    void simpleTest() {
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().nuke();
            Assertions.assertEquals(GameStatus.PLAYER1_WON, gameController.isRoundEnded());
            Assertions.assertTrue(gameController.isGameEnded().didPlayer1Won());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().surrender();
            Assertions.assertEquals(GameStatus.PLAYER1_SURRENDER, gameController.isRoundEnded());
            Assertions.assertFalse(gameController.isGameEnded().didPlayer1Won());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            for (int i = 0; i < 7; i++)
                Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            gameController.getRound().nuke();
            Assertions.assertEquals(GameStatus.PLAYER2_WON, gameController.isRoundEnded());
            Assertions.assertFalse(gameController.isGameEnded().didPlayer1Won());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            for (int i = 0; i < 7; i++)
                Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            gameController.getRound().surrender();
            Assertions.assertEquals(GameStatus.PLAYER2_SURRENDER, gameController.isRoundEnded());
            Assertions.assertTrue(gameController.isGameEnded().didPlayer1Won());
        }
        // Three round tests
        {
            GameController gameController = new GameController(user1, user2, GameRounds.THREE);
            gameController.setupNewRound();
            gameController.getRound().nuke();
            gameController.isRoundEnded();
            gameController.getRound().nuke();
            Assertions.assertEquals(GameStatus.PLAYER1_WON, gameController.isRoundEnded());
            Assertions.assertTrue(gameController.isGameEnded().didPlayer1Won());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.THREE);
            gameController.setupNewRound();
            gameController.getRound().surrender();
            Assertions.assertEquals(GameStatus.PLAYER1_SURRENDER, gameController.isRoundEnded());
            Assertions.assertFalse(gameController.isGameEnded().didPlayer1Won());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertEquals(GamePhase.STANDBY, gameController.getRound().getPhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertEquals(GamePhase.MAIN1, gameController.getRound().getPhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertEquals(GamePhase.BATTLE_PHASE, gameController.getRound().getPhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertEquals(GamePhase.MAIN2, gameController.getRound().getPhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertEquals(GamePhase.END_PHASE, gameController.getRound().getPhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertEquals("Draw", gameController.getRound().getPhase().toString());
        }
    }

    @Test
    void testNormalGameplay() {
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(CardNotExistsException.class, () -> gameController.getRound().forceDrawCard(weekCardName + "wjkijiweojgejoigewjo"));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertThrows(NoCardSelectedYetException.class, () -> gameController.getRound().deselectCard());
            Assertions.assertThrows(NoCardSelectedYetException.class, () -> gameController.getRound().getSelectedCard());
            Assertions.assertThrows(NoCardSelectedYetException.class, () -> gameController.getRound().summonCard());
            Assertions.assertThrows(NoCardSelectedException.class, () -> gameController.getRound().setCard()); // what the fuck is NoCardSelectedException and NoCardSelectedYetException
            Assertions.assertThrows(NoCardFoundInPositionException.class, () -> gameController.getRound().selectCard(10, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(weekCardName, gameController.getRound().getSelectedCard().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().setCard());
            Assertions.assertNull(gameController.getRound().getField());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(strongCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, true, CardPlaceType.MONSTER));
            Assertions.assertThrows(CardHiddenException.class, () -> gameController.getRound().getSelectedCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().deselectCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(CantAttackToPlayerException.class, () -> gameController.getRound().attackToPlayer());
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("opponent's monster card was week and the defense position monster is destroyed", gameController.getRound().attackToMonster(1).toString()));
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(strongCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().setCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(CantAttackToPlayerException.class, () -> gameController.getRound().attackToPlayer());
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("opponent's monster card was strong and no card is destroyed and you received 900 battle damage", gameController.getRound().attackToMonster(1).toString()));
            Assertions.assertEquals(8000, gameController.getRound().getRivalBoard().getPlayer().getHealth());
            Assertions.assertEquals(7100, gameController.getRound().getPlayerBoard().getPlayer().getHealth());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(strongCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(CantAttackToPlayerException.class, () -> gameController.getRound().attackToPlayer());
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("your monster card is destroyed and you received 900 battle damage", gameController.getRound().attackToMonster(1).toString()));
            Assertions.assertEquals(8000, gameController.getRound().getRivalBoard().getPlayer().getHealth());
            Assertions.assertEquals(7100, gameController.getRound().getPlayerBoard().getPlayer().getHealth());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(strongCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(CantAttackToPlayerException.class, () -> gameController.getRound().attackToPlayer());
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("your opponent's monster is destroyed and your opponent receives 900 battle damage", gameController.getRound().attackToMonster(1).toString()));
            Assertions.assertEquals(7100, gameController.getRound().getRivalBoard().getPlayer().getHealth());
            Assertions.assertEquals(8000, gameController.getRound().getPlayerBoard().getPlayer().getHealth());
        }
    }
}
