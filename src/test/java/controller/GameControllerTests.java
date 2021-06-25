package controller;

import model.Deck;
import model.User;
import model.cards.*;
import model.cards.spells.*;
import model.database.CardLoaderTest;
import model.enums.*;
import model.exceptions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

public class GameControllerTests {
    private static User user1, user2;
    private static MonsterCard monsterCardStrong, monsterCardWeek, monsterCardTribute, monsterCardRitual;
    private static final String strongCardName = "strong", weekCardName = "week", tributeCardName = "tribute", ritualCardName = "ritual";

    @BeforeAll
    static void setup() {
        CardLoaderTest.setupCards();
        monsterCardStrong = new SimpleMonster(strongCardName, "", 0, 1, 1000, 1000, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardWeek = new SimpleMonster(weekCardName, "", 0, 1, 100, 100, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardTribute = new SimpleMonster(tributeCardName, "", 0, 5, 2000, 2000, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardRitual = new RitualMonster(ritualCardName, "", 0, 6, 2500, 2000, MonsterType.AQUA, MonsterAttributeType.WATER);
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
        MonsterCard.getAllMonsterCards().remove(monsterCardRitual);
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
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertThrows(InvalidPhaseActionException.class, () -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(CardNotExistsException.class, () -> gameController.getRound().forceDrawCard(weekCardName + "wjkijiweojgejoigewjo"));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().deselectCard());
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
            Assertions.assertThrows(OnlySpellCardsAllowedException.class, () -> gameController.getRound().activeSpell());
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
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(CantAttackToPlayerException.class, () -> gameController.getRound().attackToPlayer());
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("both you and your opponent monster cards are destroyed and no one receives damage", gameController.getRound().attackToMonster(1).toString()));
            Assertions.assertEquals(8000, gameController.getRound().getRivalBoard().getPlayer().getHealth());
            Assertions.assertEquals(8000, gameController.getRound().getPlayerBoard().getPlayer().getHealth());
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
            gameController.getRound().painkiller();
            Assertions.assertEquals(7100, gameController.getRound().getRivalBoard().getPlayer().getHealth());
            Assertions.assertEquals(8000 + 8000, gameController.getRound().getPlayerBoard().getPlayer().getHealth());
            Assertions.assertThrows(NoCardFoundInPositionException.class, () -> gameController.getRound().selectCard(1, false, CardPlaceType.GRAVEYARD));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, true, CardPlaceType.GRAVEYARD));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(weekCardName, gameController.getRound().getSelectedCard().getName()));
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertThrows(NoCardSelectedYetException.class, () -> gameController.getRound().flipSummon());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertThrows(CantFlipSummonException.class, () -> gameController.getRound().flipSummon());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
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
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(tributeCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertThrows(CardAlreadyInWantedPositionException.class, () -> gameController.getRound().setCardPosition(true));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().setCardPosition(false));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertThrows(TributeNeededException.class, () -> gameController.getRound().summonCard());
            Assertions.assertThrows(TributeNeededException.class, () -> gameController.getRound().setCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard(new ArrayList<>(Collections.singletonList(1)), false));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("your opponent's monster is destroyed and your opponent receives 1000 battle damage", gameController.getRound().attackToMonster(1).toString()));
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(tributeCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertThrows(NotEnoughCardsToTributeException.class, () -> gameController.getRound().summonCard());
            Assertions.assertThrows(NotEnoughCardsToTributeException.class, () -> gameController.getRound().setCard());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().setCard());
            Assertions.assertThrows(NoCardSelectedYetException.class, () -> gameController.getRound().flipSummon());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertThrows(CantFlipSummonException.class, () -> gameController.getRound().flipSummon());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertThrows(InvalidPhaseActionException.class, () -> gameController.getRound().flipSummon());
            for (int i = 0; i < 13; i++)
                Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().flipSummon());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().specialSummon(monsterCardWeek, true);
            Assertions.assertEquals(monsterCardWeek, gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getCard());
        }
    }

    @Test
    void testSpellGameplay() {
        // Change of heart
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
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(ChangeOfHeart.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertThrows(CantSummonCardException.class, () -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().setCard());
            Assertions.assertThrows(NoCardSelectedException.class, () -> gameController.getRound().activeSpell());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.SPELL));
            Assertions.assertThrows(InvalidPhaseActionException.class, () -> gameController.getRound().activeSpell());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(ActivateSpellCallback.NORMAL, gameController.getRound().activeSpell()));
            gameController.getRound().changeOfHeartSwapOwner(gameController.getRound().getRivalBoard().getMonsterCardsList().get(0));
            Assertions.assertEquals(0, gameController.getRound().getRivalBoard().getMonsterCardsList().size());
            Assertions.assertEquals(2, gameController.getRound().getPlayerBoard().getMonsterCardsList().size());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().attackToPlayer());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(2, false, CardPlaceType.MONSTER));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().attackToPlayer());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertEquals(6900, gameController.getRound().getPlayerBoard().getPlayer().getHealth());
            Assertions.assertEquals(1, gameController.getRound().getRivalBoard().getMonsterCardsList().size());
            Assertions.assertEquals(1, gameController.getRound().getPlayerBoard().getMonsterCardsList().size());
            Assertions.assertEquals(1, gameController.getRound().getRivalBoard().getSpellCardsList().size());
        }
        // Equip
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(UnitedWeStand.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(ActivateSpellCallback.EQUIP, gameController.getRound().activeSpell()));
            gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).setEquippedCard((EquipSpellCard) gameController.getRound().returnSelectedCard().getCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertEquals(monsterCardWeek.getAttack() + 800, gameController.getRound().returnSelectedCard().getAttackPower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
            Assertions.assertEquals(monsterCardWeek.getDefence() + 800, gameController.getRound().returnSelectedCard().getDefencePower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
        }
        // Field
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(weekCardName));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().summonCard());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(Umiiruka.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(ActivateSpellCallback.DONE, gameController.getRound().activeSpell()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.MONSTER));
            Assertions.assertEquals(monsterCardWeek.getAttack() + 500, gameController.getRound().returnSelectedCard().getAttackPower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
            Assertions.assertEquals(0, gameController.getRound().returnSelectedCard().getDefencePower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(0, false, CardPlaceType.FIELD));
            Assertions.assertTrue(gameController.getRound().returnSelectedCard().getCard() instanceof Umiiruka);
        }
    }

    @Test
    void testRitualSummon() {
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
        Assertions.assertDoesNotThrow(() -> gameController.getRound().advancePhase());
        Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(AdvancedRitualArt.getCardName()));
        Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
        Assertions.assertThrows(CantSummonCardException.class, () -> gameController.getRound().summonCard());
        Assertions.assertDoesNotThrow(() -> gameController.getRound().setCard());
        Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(1, false, CardPlaceType.SPELL));
        Assertions.assertThrows(RitualSummonNotPossibleException.class, () -> gameController.getRound().activeSpell());
        Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(ritualCardName));
        Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
        Assertions.assertThrows(CantSummonCardException.class, () -> gameController.getRound().summonCard());
    }
}
