package view.menus.commands.deck;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;

public class DeckShowCommand extends DeckCommands {
    @Parameter(names = {"--side", "-s"})
    private boolean isSide;
    @Parameter(names = {"--deck-name", "-n"}, required = true)
    private String deckName;

    public String getDeckName() {
        return deckName;
    }

    public boolean isSide() {
        return isSide;
    }

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(super.removePrefix(command), "show");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
