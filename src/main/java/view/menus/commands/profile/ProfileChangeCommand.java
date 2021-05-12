package view.menus.commands.profile;

import com.beust.jcommander.Parameter;
import model.exceptions.InvalidCommandException;
import view.menus.commands.Command;
import view.menus.commands.CommandUtils;

public class ProfileChangeCommand implements Command {
    // Nothing is required here. See isValid to check for validation
    @Parameter(names = {"--nickname", "-n"})
    private String nickname;
    @Parameter(names = {"--password", "-p"})
    private boolean isPassword;
    @Parameter(names = {"--current"})
    private String password;
    @Parameter(names = {"--new"})
    private String newPassword;

    /**
     * Should we change the nickname of user?
     *
     * @return True if yes
     */
    public boolean isNicknameChange() {
        return nickname != null && !nickname.equals("");
    }

    /**
     * Should we change the password of user?
     *
     * @return True if yes
     */
    public boolean isPasswordChange() {
        return isPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public String removePrefix(String command) throws InvalidCommandException {
        return CommandUtils.removePrefixFromCommand(command, "profile change");
    }

    @Override
    public boolean isValid() {
        if ((nickname == null || nickname.equals("")) && (password == null || password.equals("")))
            return false;
        if (nickname != null && !nickname.equals("") && password != null && !password.equals(""))
            return false;
        if (password != null && !password.equals("") && (newPassword == null || newPassword.equals("")))
            return false;
        return true;
    }
}
