package view.menus.commands.game;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public class ShowGraveyardCommand implements Command {
    @Parameter(names = {"--opponent", "-o"})
    private boolean opponent;

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "show graveyard");
    }

    public boolean isOpponent() {
        return opponent;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
