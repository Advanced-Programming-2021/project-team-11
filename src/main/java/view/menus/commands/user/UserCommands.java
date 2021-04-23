package view.menus.commands.user;

import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public abstract class UserCommands implements Command {
    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "user");
    }
}
