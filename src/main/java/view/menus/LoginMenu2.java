package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.menucontrollers.LoginMenuController;
import model.User;
import model.exceptions.InvalidCommandException;
import model.exceptions.InvalidCredentialException;
import model.exceptions.NicknameExistsException;
import model.exceptions.UsernameExistsException;
import view.menus.commands.CommandUtils;
import view.menus.commands.user.UserLoginCommand;
import view.menus.commands.user.UserRegisterCommand;

public class LoginMenu2 extends Menu {

    public LoginMenu2() {
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
            if (login(command) || register(command))
                continue;
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    private boolean login(String command) {
        try {
            UserLoginCommand userLoginCommand = new UserLoginCommand();
            JCommander.newBuilder()
                    .addObject(userLoginCommand)
                    .build()
                    .parse(CommandUtils.translateCommandline(userLoginCommand.removePrefix(command)));
            User user = LoginMenuController.login(userLoginCommand.getUsername(), userLoginCommand.getPassword());
            System.out.println("user logged in successfully!");
            new MainMenu(user);
            return true;
        } catch (InvalidCommandException | ParameterException ignored) {
            return false;
        } catch (InvalidCredentialException ex) {
            System.out.println(ex.getMessage());
            return true;
        }
    }

    private boolean register(String command) {
        try {
            UserRegisterCommand userRegisterCommand = new UserRegisterCommand();
            JCommander.newBuilder()
                    .addObject(userRegisterCommand)
                    .build()
                    .parse(CommandUtils.translateCommandline(userRegisterCommand.removePrefix(command)));
            LoginMenuController.register(userRegisterCommand.getUsername(), userRegisterCommand.getPassword(), userRegisterCommand.getNickname());
            System.out.println("user created successfully!");
            return true;
        } catch (InvalidCommandException | ParameterException ignored) {
            return false;
        } catch (UsernameExistsException | NicknameExistsException ex) {
            System.out.println(ex.getMessage());
            return true;
        }
    }

    /**
     * In this menu, this function does nothing
     *
     * @param menu Have no effect
     */
    @Override
    void enterMenu(MenuNames menu) {
        if (menu == MenuNames.LOGIN)
            System.out.println(MenuUtils.MENU_NAV_FAILED);
        else
            System.out.println("please login first");
    }

    @Override
    public void printMenu() {
        System.out.println("Login Menu");
    }
}
