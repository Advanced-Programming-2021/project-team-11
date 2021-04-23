package view.menus.commands.user;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;

public class UserRegisterCommand extends UserCommands {
    @Parameter(names = {"--username", "-u"}, required = true)
    private String username;
    @Parameter(names = {"--password", "-p"}, required = true)
    private String password;
    @Parameter(names = {"--nickname", "-n"}, required = true)
    private String nickname;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(super.removePrefix(command), "create");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
