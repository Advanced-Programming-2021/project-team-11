package view.menus.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import model.enums.CardPlaceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.CommandLineArguments;
import view.menus.commands.deck.DeckSwapCardCommand;
import view.menus.commands.duelstart.DuelStartCommand;
import view.menus.commands.game.SelectCommand;
import view.menus.commands.game.SetCommand;
import view.menus.commands.game.ShowGraveyardCommand;

public class CommandsTests {
    @Test
    void testMainArgs() {
        {
            CommandLineArguments arguments = new CommandLineArguments();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse();
            Assertions.assertEquals("database.db", arguments.getDatabase());
            Assertions.assertEquals("Monster.csv", arguments.getMonstersConfigName());
            Assertions.assertEquals("Spell.csv", arguments.getSpellConfigName());
        }
        {
            CommandLineArguments arguments = new CommandLineArguments();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-m", "kir.csv", "-d", "koobs.db");
            Assertions.assertEquals("koobs.db", arguments.getDatabase());
            Assertions.assertEquals("kir.csv", arguments.getMonstersConfigName());
            Assertions.assertEquals("Spell.csv", arguments.getSpellConfigName());
        }
    }

    @Test
    void testDeckSwapCardCommand() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("a", new DeckSwapCardCommand().removePrefix("deck swap a")));
        {
            DeckSwapCardCommand arguments = new DeckSwapCardCommand();
            Assertions.assertThrows(ParameterException.class, () -> JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse());
        }
        {
            DeckSwapCardCommand arguments = new DeckSwapCardCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-m", "1", "-s", "2");
            Assertions.assertEquals("1", arguments.getMainDeckCardName());
            Assertions.assertEquals("2", arguments.getSideDeckCardName());
        }
    }

    @Test
    void testDuelStart() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("a", new DuelStartCommand().removePrefix("duel a")));
        {
            DuelStartCommand arguments = new DuelStartCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-n", "-r", "0");
            Assertions.assertFalse(arguments.isValid());
        }
        {
            DuelStartCommand arguments = new DuelStartCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-n", "-r", "0");
            Assertions.assertFalse(arguments.isValid());
        }
        {
            DuelStartCommand arguments = new DuelStartCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-n", "-r", "0", "-s", "nigga", "--ai");
            Assertions.assertFalse(arguments.isValid());
        }
        {
            DuelStartCommand arguments = new DuelStartCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-n", "-r", "0", "-s", "nigga");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertFalse(arguments.isAi());
            Assertions.assertEquals("nigga", arguments.getSecondPlayerName());
            Assertions.assertEquals(0, arguments.getRounds());
        }
    }

    @Test
    void testSelect() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("a", new SelectCommand().removePrefix("select a")));
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-d", "-m", "1");
            Assertions.assertFalse(arguments.isValid());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-o", "-h", "1");
            Assertions.assertFalse(arguments.isValid());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-o", "-h", "1");
            Assertions.assertFalse(arguments.isValid());
            Assertions.assertFalse(arguments.isSelectionValid());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-h", "1");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertEquals(CardPlaceType.HAND, arguments.getCardPlaceType());
            Assertions.assertEquals(1, arguments.getIndex());
            Assertions.assertTrue(arguments.isSelectionValid());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-d");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertTrue(arguments.isDeselect());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-o", "-s", "1");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertEquals(CardPlaceType.SPELL, arguments.getCardPlaceType());
            Assertions.assertEquals(1, arguments.getIndex());
            Assertions.assertTrue(arguments.isSelectionValid());
            Assertions.assertTrue(arguments.isOpponent());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse();
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertFalse(arguments.isSelectionValid());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-f");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertTrue(arguments.isSelectionValid());
            Assertions.assertEquals(CardPlaceType.FIELD, arguments.getCardPlaceType());
            Assertions.assertTrue(arguments.isField());
        }
        {
            SelectCommand arguments = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-m", "1");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertTrue(arguments.isSelectionValid());
            Assertions.assertEquals(CardPlaceType.MONSTER, arguments.getCardPlaceType());
            Assertions.assertEquals(1, arguments.getIndex());
        }
    }

    @Test
    void testSet() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("a", new SetCommand().removePrefix("set a")));
        {
            SetCommand arguments = new SetCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-p", "x");
            Assertions.assertFalse(arguments.isValid());
        }
        {
            SetCommand arguments = new SetCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse();
            Assertions.assertFalse(arguments.isValid());
        }
        {
            SetCommand arguments = new SetCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-p", "defence");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertEquals("defence", arguments.getPosition());
        }
    }

    @Test
    void testGraveyard() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals("a", new ShowGraveyardCommand().removePrefix("show graveyard a")));
        Assertions.assertTrue(new ShowGraveyardCommand().isValid());
        {
            ShowGraveyardCommand arguments = new ShowGraveyardCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse("-o");
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertTrue(arguments.isOpponent());
        }
        {
            ShowGraveyardCommand arguments = new ShowGraveyardCommand();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse();
            Assertions.assertTrue(arguments.isValid());
            Assertions.assertFalse(arguments.isOpponent());
        }
    }
}
