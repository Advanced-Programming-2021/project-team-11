package view.menus.commands.deck;

import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;

public class DeckCreateCommand extends DeckCommands {
    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(super.removePrefix(command), "create");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
