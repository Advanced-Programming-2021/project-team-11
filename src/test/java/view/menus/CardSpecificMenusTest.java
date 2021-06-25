package view.menus;

import controller.GameController;
import model.Deck;
import model.PlayableCard;
import model.User;
import model.cards.*;
import model.cards.monsters.*;
import model.cards.spells.*;
import model.cards.traps.CallOfTheHaunted;
import model.cards.traps.MindCrush;
import model.cards.traps.NegateAttack;
import model.database.CardLoaderTest;
import model.enums.CardPlaceType;
import model.enums.GameRounds;
import model.exceptions.CantActivateSpellException;
import org.junit.jupiter.api.*;

public class CardSpecificMenusTest {
    private static User user1, user2;
    private static MonsterCard monsterCardStrong, monsterCardWeek, monsterCardTribute, monsterCardRitual;
    private static final String strongCardName = "strong", weekCardName = "week", tributeCardName = "tribute", ritualCardName = "ritual";

    @BeforeAll
    static void setup() {
        CardLoaderTest.setupCards();
        Setuper.setup();
        monsterCardStrong = new SimpleMonster(strongCardName, "", 0, 1, 1000, 1000, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardWeek = new SimpleMonster(weekCardName, "", 0, 1, 100, 100, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardTribute = new SimpleMonster(tributeCardName, "", 0, 5, 2000, 2000, MonsterType.AQUA, MonsterAttributeType.WATER);
        monsterCardRitual = new RitualMonster(ritualCardName, "", 0, 7, 2500, 2000, MonsterType.AQUA, MonsterAttributeType.WATER);
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
    static void cleanUp() {
        MonsterCard.getAllMonsterCards().remove(monsterCardStrong);
        MonsterCard.getAllMonsterCards().remove(monsterCardWeek);
        MonsterCard.getAllMonsterCards().remove(monsterCardTribute);
        MonsterCard.getAllMonsterCards().remove(monsterCardRitual);
        User.getUsers().clear();
        Setuper.restore();
    }

    @BeforeEach
    void setUp() {
        Setuper.reset();
    }

    @Test
    void testHandleManEaterBugRemoval() {
        Setuper.setInput("0\na\n3\ncancel\n1\n4\n1\n2\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().specialSummon(ManEaterBug.getInstance(), true);
        CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard()); // does nothing
        Assertions.assertEquals(0, gameController.getRound().getRivalBoard().countActiveMonsterCards());
        gameController.getRound().specialSummon(monsterCardWeek, false);
        CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard()); // does nothing
        Assertions.assertEquals(1, gameController.getRound().getRivalBoard().countActiveMonsterCards());
        CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard()); // does nothing
        Assertions.assertEquals(1, gameController.getRound().getRivalBoard().countActiveMonsterCards());
        CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard()); // does nothing
        Assertions.assertEquals(0, gameController.getRound().getRivalBoard().countActiveMonsterCards());
        // A little more cards to check positions
        gameController.getRound().specialSummon(monsterCardWeek, false);
        gameController.getRound().specialSummon(monsterCardStrong, false);
        gameController.getRound().specialSummon(monsterCardTribute, false);
        gameController.getRound().specialSummon(monsterCardRitual, false);
        CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard()); // does nothing
        Assertions.assertEquals(0, gameController.getRound().getRivalBoard().getMonsterCardsList().stream().filter(card -> card.getCard() == monsterCardRitual).count());
        CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard()); // does nothing
        Assertions.assertEquals(0, gameController.getRound().getRivalBoard().getMonsterCardsList().stream().filter(card -> card.getCard() == monsterCardWeek).count());
        CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard()); // does nothing
        Assertions.assertEquals(0, gameController.getRound().getRivalBoard().getMonsterCardsList().stream().filter(card -> card.getCard() == monsterCardStrong).count());
    }

    @Test
    void testHandleScannerCardEffect() {
        Setuper.setInput("igej\n1000\n-1\ncancel\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().specialSummon(ScannerCard.getInstance(), true);
        Assertions.assertThrows(CantActivateSpellException.class, () -> CardSpecificMenus.handleScannerCardEffect(gameController.getRound().getRivalBoard().getGraveyard(), gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0)));
        gameController.getRound().getRivalBoard().getGraveyard().add(new PlayableCard(monsterCardWeek, CardPlaceType.GRAVEYARD));
        gameController.getRound().getRivalBoard().getGraveyard().add(new PlayableCard(monsterCardStrong, CardPlaceType.GRAVEYARD));
        gameController.getRound().getRivalBoard().getGraveyard().add(new PlayableCard(monsterCardTribute, CardPlaceType.GRAVEYARD));
        gameController.getRound().getRivalBoard().getGraveyard().add(new PlayableCard(monsterCardRitual, CardPlaceType.GRAVEYARD));
        Assertions.assertDoesNotThrow(() -> CardSpecificMenus.handleScannerCardEffect(gameController.getRound().getRivalBoard().getGraveyard(), gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0)));
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getCard() instanceof ScannerCard);
        Assertions.assertDoesNotThrow(() -> CardSpecificMenus.handleScannerCardEffect(gameController.getRound().getRivalBoard().getGraveyard(), gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0)));
        Assertions.assertEquals(weekCardName, gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getCard().getName());
        Assertions.assertEquals("rival's graveyard\n" +
                "1. week:\n" +
                "2. strong:\n" +
                "3. tribute:\n" +
                "4. ritual:\n" +
                "Choose a card by it's index: invalid number\n" +
                "Please select a monster card\n" +
                "Choose a card by it's index: Please select a monster card\n" +
                "Choose a card by it's index: Please select a monster card\n" +
                "Choose a card by it's index: rival's graveyard\n" +
                "1. week:\n" +
                "2. strong:\n" +
                "3. tribute:\n" +
                "4. ritual:\n" +
                "Choose a card by it's index: Your scanner is now week!\n", Setuper.getOutputString());
    }

    @Test
    void testSummonBeastKingBarbaros() {
        Setuper.setInput("a\n-1\n1\n2\n0\n2\n2\n1\n3\n1\n2\n3\n3\n1\n2\n5\n2\ncancel\n");
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(BeastKingBarbaros.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertTrue(CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound()));
            Assertions.assertEquals(1900, gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getAttackPower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, true);
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(BeastKingBarbaros.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertTrue(CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound()));
            Assertions.assertEquals(3000, gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getAttackPower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
            Assertions.assertEquals(1, gameController.getRound().getPlayerBoard().countActiveMonsterCards());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, false);
            gameController.getRound().specialSummon(monsterCardWeek, false);
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(BeastKingBarbaros.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertTrue(CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound()));
            Assertions.assertEquals(3000, gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getAttackPower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
            Assertions.assertEquals(1, gameController.getRound().getPlayerBoard().countActiveMonsterCards());
            Assertions.assertEquals(0, gameController.getRound().getRivalBoard().countActiveMonsterCards());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, false);
            gameController.getRound().specialSummon(monsterCardWeek, false);
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(BeastKingBarbaros.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertFalse(CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound()));
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, true);
            gameController.getRound().specialSummon(monsterCardWeek, false);
            gameController.getRound().specialSummon(monsterCardWeek, false);
            Assertions.assertDoesNotThrow(() -> gameController.getRound().forceDrawCard(BeastKingBarbaros.getInstance().getName()));
            Assertions.assertDoesNotThrow(() -> gameController.getRound().selectCard(7, false, CardPlaceType.HAND));
            Assertions.assertFalse(CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound()));
        }
        Assertions.assertEquals("How many cards you want to tribute? (0/2/3) invalid number\n" +
                "How many cards you want to tribute? (0/2/3) Invalid tributes number!\n" +
                "How many cards you want to tribute? (0/2/3) Invalid tributes number!\n" +
                "How many cards you want to tribute? (0/2/3) there are not enough cards for tribute\n" +
                "How many cards you want to tribute? (0/2/3) How many cards you want to tribute? (0/2/3) Select a card position to tribute or type \"cancel\" to cancel (0/2): Select a card position to tribute or type \"cancel\" to cancel (1/2): How many cards you want to tribute? (0/2/3) Select a card position to tribute or type \"cancel\" to cancel (0/3): Select a card position to tribute or type \"cancel\" to cancel (1/3): Select a card position to tribute or type \"cancel\" to cancel (2/3): How many cards you want to tribute? (0/2/3) Select a card position to tribute or type \"cancel\" to cancel (0/3): Select a card position to tribute or type \"cancel\" to cancel (1/3): Select a card position to tribute or type \"cancel\" to cancel (2/3): there is no monster on one of these addresses\n" +
                "How many cards you want to tribute? (0/2/3) Select a card position to tribute or type \"cancel\" to cancel (0/2): ", Setuper.getOutputString());
    }

    @Test
    void testSummonCardWithHeraldOfCreation() {
        Setuper.setInput("0\n100\ncancel\n1\ncancel\n1\n0\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().specialSummon(HeraldOfCreation.getInstance(), true);
        gameController.getRound().getPlayerBoard().getGraveyard().add(new PlayableCard(monsterCardRitual, CardPlaceType.GRAVEYARD));
        CardSpecificMenus.summonCardWithHeraldOfCreation(gameController.getRound().getPlayerBoard(), gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0));
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getHand().stream().noneMatch(card -> card.getCard() == monsterCardRitual));
        CardSpecificMenus.summonCardWithHeraldOfCreation(gameController.getRound().getPlayerBoard(), gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0));
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getHand().stream().noneMatch(card -> card.getCard() == monsterCardRitual));
        CardSpecificMenus.summonCardWithHeraldOfCreation(gameController.getRound().getPlayerBoard(), gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0));
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getHand().stream().anyMatch(card -> card.getCard() == monsterCardRitual));
    }

    @Test
    void testTerratigerTheEmpoweredWarriorSummon() {
        Setuper.setInput("-1\n10000\ncancel\n1\n");
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().getPlayerBoard().getHand().clear();
            CardSpecificMenus.handleTerratigerTheEmpoweredWarriorSummon(gameController.getRound().getPlayerBoard());
            gameController.getRound().getPlayerBoard().getHand().add(new PlayableCard(monsterCardWeek, CardPlaceType.HAND));
            CardSpecificMenus.handleTerratigerTheEmpoweredWarriorSummon(gameController.getRound().getPlayerBoard());
            Assertions.assertEquals(0, gameController.getRound().getPlayerBoard().countActiveMonsterCards());
            CardSpecificMenus.handleTerratigerTheEmpoweredWarriorSummon(gameController.getRound().getPlayerBoard());
            Assertions.assertEquals(1, gameController.getRound().getPlayerBoard().countActiveMonsterCards());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            for (int i = 0; i < 5; i++)
                gameController.getRound().specialSummon(monsterCardWeek, true);
            CardSpecificMenus.handleTerratigerTheEmpoweredWarriorSummon(gameController.getRound().getPlayerBoard());
        }
        Assertions.assertEquals("1. week\n" +
                "Choose a card by it's index: invalid number\n" +
                "Choose a card by it's index: invalid number\n" +
                "Choose a card by it's index: 1. week\n" +
                "Choose a card by it's index: week summoned successfully!\n", Setuper.getOutputString());
    }

    @Test
    void testSpawnTheTricky() {
        Setuper.setInput("a\n0\n1000\n1\n2\n");
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            gameController.getRound().getPlayerBoard().getHand().clear();
            PlayableCard trickyCard = new PlayableCard(TheTricky.getInstance(), CardPlaceType.HAND);
            gameController.getRound().getPlayerBoard().getHand().add(trickyCard);
            Assertions.assertFalse(CardSpecificMenus.spawnTheTricky(gameController.getRound().getPlayerBoard(), trickyCard));
            gameController.getRound().getPlayerBoard().getHand().add(new PlayableCard(monsterCardWeek, CardPlaceType.HAND));
            Assertions.assertTrue(CardSpecificMenus.spawnTheTricky(gameController.getRound().getPlayerBoard(), trickyCard));
            Assertions.assertTrue(gameController.getRound().getPlayerBoard().getMonsterCardsList().contains(trickyCard));
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            for (int i = 0; i < 5; i++)
                gameController.getRound().specialSummon(monsterCardWeek, true);
            Assertions.assertFalse(CardSpecificMenus.spawnTheTricky(gameController.getRound().getPlayerBoard(), null));
        }
        Assertions.assertEquals("Your hand is empty!\n" +
                "1. The Tricky\n" +
                "2. week\n" +
                "invalid number\n" +
                "invalid number\n" +
                "invalid number\n" +
                "You played yourself!\n" +
                "monster card zone is full\n", Setuper.getOutputString());
    }

    @Test
    void testHandleRitualSpawn() {
        RitualMonster ritualMonsterHighLevel = new RitualMonster("big nigga", "", 0, 10, 2500, 2000, MonsterType.AQUA, MonsterAttributeType.WATER);
        Setuper.setInput("0\n10000\n2\n1\n1,2,3000\na\n1,2,3\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        PlayableCard ritualCard = new PlayableCard(AdvancedRitualArt.getInstance(), CardPlaceType.SPELL);
        gameController.getRound().getPlayerBoard().addSpellCard(ritualCard);
        gameController.getRound().getPlayerBoard().addMonsterCard(new PlayableCard(monsterCardWeek, CardPlaceType.MONSTER));
        gameController.getRound().getPlayerBoard().addMonsterCard(new PlayableCard(monsterCardWeek, CardPlaceType.MONSTER));
        gameController.getRound().getPlayerBoard().addMonsterCard(new PlayableCard(monsterCardTribute, CardPlaceType.MONSTER));
        gameController.getRound().getPlayerBoard().getHand().clear();
        gameController.getRound().getPlayerBoard().getHand().add(new PlayableCard(monsterCardRitual, CardPlaceType.HAND));
        gameController.getRound().getPlayerBoard().getHand().add(new PlayableCard(ritualMonsterHighLevel, CardPlaceType.HAND));
        CardSpecificMenus.handleRitualSpawn(gameController.getRound().getPlayerBoard(), ritualCard);
        MonsterCard.getAllMonsterCards().remove(ritualMonsterHighLevel);
        Assertions.assertEquals("1. ritual\n" +
                "2. big nigga\n" +
                "Choose a card by it's index: invalid number\n" +
                "Choose a card by it's index: invalid number\n" +
                "Choose a card by it's index: You can't ritual summon this card!\n" +
                "Choose a card by it's index: 1. week -> level 1\n" +
                "2. week -> level 1\n" +
                "3. tribute -> level 5\n" +
                "Choose some monsters by their index, separated by ',': One of your cards position is not valid!\n" +
                "Choose some monsters by their index, separated by ',': invalid number\n" +
                "Choose some monsters by their index, separated by ',': ", Setuper.getOutputString());
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getHand().stream().noneMatch(x -> x.getCard() == monsterCardRitual));
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getHand().stream().anyMatch(x -> x.getCard() == ritualMonsterHighLevel));
        Assertions.assertEquals(1, gameController.getRound().getPlayerBoard().countActiveMonsterCards());
    }

    @Test
    void testHandleMonsterReborn() {
        Setuper.setInput("cancel\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().getPlayerBoard().getGraveyard().add(new PlayableCard(monsterCardWeek, CardPlaceType.GRAVEYARD));
        PlayableCard monsterReborn = new PlayableCard(MonsterReborn.getInstance(), CardPlaceType.SPELL);
        gameController.getRound().getPlayerBoard().addSpellCard(monsterReborn);
        CardSpecificMenus.handleMonsterReborn(gameController.getRound(), monsterReborn);
        Assertions.assertFalse(gameController.getRound().getPlayerBoard().getMonsterCardsList().stream().anyMatch(card -> card.getCard() == monsterCardWeek));
        Assertions.assertFalse(gameController.getRound().getPlayerBoard().getSpellCardsList().isEmpty());
        CardSpecificMenus.handleMonsterReborn(gameController.getRound(), monsterReborn);
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getMonsterCardsList().stream().anyMatch(card -> card.getCard() == monsterCardWeek));
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getSpellCardsList().isEmpty());
    }

    @Test
    void testHandleTerraforming() {
        Setuper.setInput("cancel\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().getPlayerBoard().getDeck().add(ClosedForest.getInstance());
        PlayableCard terraforming = new PlayableCard(Terraforming.getInstance(), CardPlaceType.SPELL);
        gameController.getRound().getPlayerBoard().addSpellCard(terraforming);
        CardSpecificMenus.handleTerraforming(gameController.getRound().getPlayerBoard(), terraforming);
        Assertions.assertFalse(gameController.getRound().getPlayerBoard().getSpellCardsList().isEmpty());
        Assertions.assertFalse(gameController.getRound().getPlayerBoard().getHand().stream().anyMatch(x -> x.getCard() == Terraforming.getInstance()));
        CardSpecificMenus.handleTerraforming(gameController.getRound().getPlayerBoard(), terraforming);
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getSpellCardsList().isEmpty());
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getHand().stream().noneMatch(x -> x.getCard() == Terraforming.getInstance()));
    }

    @Test
    void testHandleChangeOfHeart() {
        Setuper.setInput("cancel\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().getRivalBoard().addMonsterCard(new PlayableCard(monsterCardWeek, CardPlaceType.MONSTER));
        gameController.getRound().getRivalBoard().addMonsterCard(new PlayableCard(monsterCardTribute, CardPlaceType.MONSTER));
        gameController.getRound().getRivalBoard().addMonsterCard(new PlayableCard(monsterCardStrong, CardPlaceType.MONSTER));
        gameController.getRound().getRivalBoard().addMonsterCard(new PlayableCard(monsterCardRitual, CardPlaceType.MONSTER));
        PlayableCard card = new PlayableCard(ChangeOfHeart.getInstance(), CardPlaceType.SPELL);
        gameController.getRound().getPlayerBoard().addSpellCard(card);
        CardSpecificMenus.handleChangeOfHeart(gameController.getRound(), card);
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getSpellCardsList().stream().anyMatch(x -> x.getCard() == ChangeOfHeart.getInstance()));
        CardSpecificMenus.handleChangeOfHeart(gameController.getRound(), card);
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getSpellCardsList().stream().noneMatch(x -> x.getCard() == ChangeOfHeart.getInstance()));
    }

    @Test
    void testEquip() {
        Setuper.setInput("cancel\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        PlayableCard monster = new PlayableCard(monsterCardWeek, CardPlaceType.MONSTER);
        monster.setAttacking();
        monster.makeVisible();
        gameController.getRound().getPlayerBoard().addMonsterCard(monster);
        PlayableCard card = new PlayableCard(UnitedWeStand.getInstance(), CardPlaceType.MONSTER);
        gameController.getRound().getPlayerBoard().addSpellCard(card);
        CardSpecificMenus.equip(gameController.getRound(), card);
        CardSpecificMenus.equip(gameController.getRound(), card);
        Assertions.assertEquals(monsterCardWeek.getAttack() + 800, gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getAttackPower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
        Assertions.assertEquals(monsterCardWeek.getDefence() + 800, gameController.getRound().getPlayerBoard().getMonsterCardsList().get(0).getDefencePower(gameController.getRound().getPlayerBoard(), gameController.getRound().getField()));
    }

    @Test
    void testCallOfTheHunted() {
        Setuper.setInput("cancel\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().getPlayerBoard().getGraveyard().add(new PlayableCard(monsterCardWeek, CardPlaceType.GRAVEYARD));
        PlayableCard spell = new PlayableCard(CallOfTheHaunted.getInstance(), CardPlaceType.SPELL);
        gameController.getRound().getPlayerBoard().addSpellCard(spell);
        CardSpecificMenus.callOfTheHunted(gameController.getRound().getPlayerBoard(), spell);
        Assertions.assertFalse(gameController.getRound().getPlayerBoard().getMonsterCardsList().stream().anyMatch(card -> card.getCard() == monsterCardWeek));
        Assertions.assertFalse(gameController.getRound().getPlayerBoard().getSpellCardsList().isEmpty());
        CardSpecificMenus.callOfTheHunted(gameController.getRound().getPlayerBoard(), spell);
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getMonsterCardsList().stream().anyMatch(card -> card.getCard() == monsterCardWeek));
        Assertions.assertTrue(gameController.getRound().getPlayerBoard().getSpellCardsList().isEmpty());
    }

    @Test
    void testActivateTrapNegateAttack() {
        Setuper.setInput("n\ny\n2\ny\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        gameController.getRound().getRivalBoard().addSpellCard(new PlayableCard(NegateAttack.getInstance(), CardPlaceType.SPELL));
        PlayableCard rivalCard = new PlayableCard(monsterCardWeek, CardPlaceType.MONSTER);
        gameController.getRound().getPlayerBoard().addMonsterCard(rivalCard);
        Assertions.assertFalse(CardSpecificMenus.activateTrap(gameController.getRound(), new String[]{NegateAttack.getInstance().getName()}, rivalCard));
        Assertions.assertFalse(CardSpecificMenus.activateTrap(gameController.getRound(), new String[]{NegateAttack.getInstance().getName()}, rivalCard));
        Assertions.assertTrue(CardSpecificMenus.activateTrap(gameController.getRound(), new String[]{NegateAttack.getInstance().getName()}, rivalCard));
        Assertions.assertEquals("Do you want to activate your trap or spell? (y/n)\n" +
                "Do you want to activate your trap or spell? (y/n)\n" +
                "Select a card:\n" +
                "1. Negate Attack\n" +
                "2. cancel the activation\n" +
                "Choose a card by it's index: Do you want to activate your trap or spell? (y/n)\n" +
                "Select a card:\n" +
                "1. Negate Attack\n" +
                "2. cancel the activation\n" +
                "Choose a card by it's index: Next phase!\n", Setuper.getOutputString());
    }

    @Test
    void testMindCrushCard() {
        Setuper.setInput("kirgjeiowgweoigjew\ncancel\n" + MindCrush.getInstance().getName() + "\n" + strongCardName + "\n");
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            PlayableCard mindCrush = new PlayableCard(MindCrush.getInstance(), CardPlaceType.SPELL);
            gameController.getRound().getRivalBoard().addSpellCard(mindCrush);
            CardSpecificMenus.getMindCrushCard(gameController.getRound(), mindCrush);
            Assertions.assertEquals(6, gameController.getRound().getPlayerBoard().getHand().size());
            CardSpecificMenus.getMindCrushCard(gameController.getRound(), mindCrush);
            Assertions.assertEquals(5, gameController.getRound().getPlayerBoard().getHand().size());
        }
        {
            GameController gameController = new GameController(user1, user2, GameRounds.ONE);
            gameController.setupNewRound();
            PlayableCard mindCrush = new PlayableCard(MindCrush.getInstance(), CardPlaceType.SPELL);
            gameController.getRound().getRivalBoard().addSpellCard(mindCrush);
            CardSpecificMenus.getMindCrushCard(gameController.getRound(), mindCrush);
            Assertions.assertEquals(6, gameController.getRound().getPlayerBoard().getHand().size());
            Assertions.assertTrue(gameController.getRound().getRivalBoard().getHand().stream().noneMatch(x -> x.getCard() == monsterCardStrong));
            Assertions.assertTrue(gameController.getRound().getRivalBoard().getDeck().stream().noneMatch(x -> x == monsterCardStrong));
        }
    }

    @Test
    void testHandleTwinTwisters() {
        Setuper.setInput("2\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        PlayableCard card = new PlayableCard(TwinTwisters.getInstance(), CardPlaceType.SPELL);
        gameController.getRound().getPlayerBoard().addSpellCard(card);
        gameController.getRound().getRivalBoard().addSpellCard(new PlayableCard(UnitedWeStand.getInstance(), CardPlaceType.MONSTER));
        gameController.getRound().getRivalBoard().addSpellCard(new PlayableCard(CallOfTheHaunted.getInstance(), CardPlaceType.MONSTER));
        CardSpecificMenus.handleTwinTwisters(gameController.getRound(), card);
        Assertions.assertEquals(0, gameController.getRound().getRivalBoard().getSpellCardsList().size());
    }

    @Test
    void testMysticalSpaceTyphoon() {
        Setuper.setInput("cancel\n1\n");
        GameController gameController = new GameController(user1, user2, GameRounds.ONE);
        gameController.setupNewRound();
        PlayableCard card = new PlayableCard(TwinTwisters.getInstance(), CardPlaceType.SPELL);
        gameController.getRound().getPlayerBoard().addSpellCard(card);
        gameController.getRound().getRivalBoard().addSpellCard(new PlayableCard(UnitedWeStand.getInstance(), CardPlaceType.SPELL));
        CardSpecificMenus.handleMysticalSpaceTyphoon(gameController.getRound(), card);
        Assertions.assertEquals(1, gameController.getRound().getRivalBoard().getSpellCardsList().size());
        CardSpecificMenus.handleMysticalSpaceTyphoon(gameController.getRound(), card);
        Assertions.assertEquals(0, gameController.getRound().getRivalBoard().getSpellCardsList().size());
    }
}
