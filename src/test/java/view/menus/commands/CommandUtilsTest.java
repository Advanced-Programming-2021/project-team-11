package view.menus.commands;

import model.exceptions.InvalidCommandException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandUtilsTest {
    @Test
    void removePrefixTest() {
        try {
            String result = CommandUtils.removePrefixFromCommand("hello", "hello");
            Assertions.fail("prefix removed: " + result);
        } catch (InvalidCommandException ignored) {
        }
        try {
            String result = CommandUtils.removePrefixFromCommand("hello ", "hello");
            Assertions.assertEquals("", result);
        } catch (InvalidCommandException ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    void translateCommandlineTest() {
        Assertions.assertArrayEquals(new String[0], CommandUtils.translateCommandline(null));
        Assertions.assertArrayEquals(new String[]{"test", "test 1"}, CommandUtils.translateCommandline("test \"test 1\""));
        Assertions.assertArrayEquals(new String[]{"test", "test 1"}, CommandUtils.translateCommandline("test 'test 1'"));
        try {
            CommandUtils.translateCommandline("test 'test 1''");
            Assertions.fail("malformed data parsed");
        } catch (Exception ignored) {
        }
    }
}
