package view.menus.commands.deck;

import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;

public class DeckDeleteCommand extends DeckCommands {
    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(super.removePrefix(command), "delete");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
