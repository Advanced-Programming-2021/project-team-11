package view.menus.commands.game;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public class SetCommand implements Command {
    @Parameter(names = {"--position", "-p"})
    private String position;

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "select");
    }

    public String getPosition() {
        return position;
    }

    @Override
    public boolean isValid() {
        return position == null || position.equals("") || position.equals("attack") || position.equals("defence");
    }
}
