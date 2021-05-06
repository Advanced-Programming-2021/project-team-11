package view.menus.commands.deck;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public class DeckSwapCardCommand implements Command {
    @Parameter(names = {"--main", "-m"}, required = true)
    private String mainDeckCard;
    @Parameter(names = {"--side", "-s"}, required = true)
    private String sideDeckCard;

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "deck swap");
    }

    public String getMainDeckCardName() {
        return mainDeckCard;
    }

    public String getSideDeckCardName() {
        return sideDeckCard;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
