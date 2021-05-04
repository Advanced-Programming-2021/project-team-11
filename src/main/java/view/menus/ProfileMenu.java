package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.menucontrollers.ProfileMenuController;
import model.User;
import model.exceptions.*;
import view.menus.commands.profile.ProfileChangeCommand;

public class ProfileMenu extends Menu {
    private final User loggedInUser;

    ProfileMenu(User loggedInUser) {
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
            if (processCommand(command))
                continue;
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    private boolean processCommand(String command) {
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
            return true;
        } catch (InvalidCommandException | ParameterException ignored) {
            return false;
        } catch (NicknameExistsException | CurrentPasswordInvalidException | SameNewPasswordException ex) {
            System.out.println(ex.getMessage());
            return true;
        }
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Profile Menu");
    }
}
