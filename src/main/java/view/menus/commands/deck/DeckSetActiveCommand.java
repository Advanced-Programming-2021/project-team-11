package view.menus.commands.deck;

import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;

public class DeckSetActiveCommand extends DeckCommands {
    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(super.removePrefix(command), "set-activate");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
