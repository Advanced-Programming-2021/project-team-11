package view.menus.commands.user;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;

public class UserLoginCommand extends UserCommands {
    @Parameter(names = {"--username", "-u"}, required = true)
    private String username;
    @Parameter(names = {"--password", "-p"}, required = true)
    private String password;

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(super.removePrefix(command), "login");
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
