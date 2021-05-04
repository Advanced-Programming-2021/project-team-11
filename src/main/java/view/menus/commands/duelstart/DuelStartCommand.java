package view.menus.commands.duelstart;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public class DuelStartCommand implements Command {
    @Parameter(names = {"--new", "-n"}, required = true)
    private boolean newCommand;
    @Parameter(names = {"--rounds", "-r"}, required = true)
    private int rounds;
    @Parameter(names = {"--ai"})
    private boolean ai;
    @Parameter(names = {"--second-player", "-s"})
    private String secondPlayer;

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "duel");
    }

    public int getRounds() {
        return rounds;
    }

    public String getSecondPlayerName() {
        return secondPlayer;
    }

    public boolean isAi() {
        return ai;
    }

    @Override
    public boolean isValid() {
        // Check the round in view
        if (!ai && (secondPlayer == null || secondPlayer.equals("")))
            return false;
        if (ai && (secondPlayer != null && !secondPlayer.equals("")))
            return false;
        return true;
    }
}
