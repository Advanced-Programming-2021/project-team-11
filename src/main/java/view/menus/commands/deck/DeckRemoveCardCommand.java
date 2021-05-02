package view.menus.commands.deck;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;

public class DeckRemoveCardCommand extends DeckCommands {
    @Parameter(names = {"--card", "-c"}, required = true)
    private String cardName;
    @Parameter(names = {"--deck", "-d"}, required = true)
    private String deckName;
    @Parameter(names = {"--side", "-s"})
    private boolean isSide;

    public boolean isSide() {
        return isSide;
    }

    public String getCardName() {
        return cardName;
    }

    public String getDeckName() {
        return deckName;
    }

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(super.removePrefix(command), "rm-card");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
