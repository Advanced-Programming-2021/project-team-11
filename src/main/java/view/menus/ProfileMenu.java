package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.menucontrollers.ProfileMenuController;
import model.User;
import model.exceptions.*;
import view.menus.commands.profile.ProfileChangeCommand;

public class ProfileMenu extends Menu {
    private final User loggedInUser;

    public ProfileMenu(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        openMenu();
    }

    @Override
    void openMenu() {
        while (true) {
            String command = MenuUtils.readLine();
            try {
                if (processMenuCommands(command))
                    return;
                continue;
            } catch (InvalidCommandException ignored) {
            }
            try {
                ProfileChangeCommand profileChangeCommand = new ProfileChangeCommand();
                JCommander.newBuilder()
                        .addObject(profileChangeCommand)
                        .build()
                        .parse(profileChangeCommand.removePrefix(command).split(" "));
                if (!profileChangeCommand.isValid())
                    throw new InvalidCommandException();
                if (profileChangeCommand.isNicknameChange()) {
                    ProfileMenuController.changeNickname(loggedInUser, profileChangeCommand.getNickname());
                    System.out.println("nickname changed successfully!");
                } else if (profileChangeCommand.isPasswordChange()) {
                    ProfileMenuController.changePassword(loggedInUser, profileChangeCommand.getPassword(), profileChangeCommand.getNewPassword());
                    System.out.println("password changed successfully!");
                } else {
                    throw new BooAnException("profile not change password, not change nickname and not invalid!");
                }
                continue;
            } catch (InvalidCommandException | ParameterException ignored) {
            } catch (NicknameExistsException | CurrentPasswordInvalidException | SameNewPasswordException ex) {
                System.out.println(ex.getMessage());
                continue;
            }
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    @Override
    void enterMenu(MenuNames menu) {

    }

    @Override
    void printMenu() {
        System.out.println("Profile Menu");
    }
}
