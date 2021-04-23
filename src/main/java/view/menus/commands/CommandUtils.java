package view.menus.commands;

import model.exceptions.InvalidCommandException;

public class CommandUtils {
    public static String removePrefixFromCommand(String command, String prefix) throws InvalidCommandException {
        prefix += " ";
        if (!command.startsWith(prefix))
            throw new InvalidCommandException();
        return command.substring(prefix.length());
    }
}
