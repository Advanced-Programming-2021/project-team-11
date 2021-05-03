package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.menucontrollers.LoginMenuController;
import model.User;
import model.exceptions.InvalidCommandException;
import model.exceptions.InvalidCredentialException;
import model.exceptions.NicknameExistsException;
import model.exceptions.UsernameExistsException;
import view.menus.commands.user.UserLoginCommand;
import view.menus.commands.user.UserRegisterCommand;

public class LoginMenu extends Menu {

    public LoginMenu() {
        openMenu();
    }

    @Override
    public void openMenu() {
        while (true) {
            String command = MenuUtils.readLine();
            try {
                if (processMenuCommands(command))
                    return;
                continue;
            } catch (InvalidCommandException ignored) {
            }
            // Try to login
            try {
                UserLoginCommand userLoginCommand = new UserLoginCommand();
                JCommander.newBuilder()
                        .addObject(userLoginCommand)
                        .build()
                        .parse(userLoginCommand.removePrefix(command).split(" "));
                User user = LoginMenuController.login(userLoginCommand.getUsername(), userLoginCommand.getPassword());
                System.out.println("user logged in successfully!");
                new MainMenu(user);
                continue;
            } catch (InvalidCommandException | ParameterException ignored) {
            } catch (InvalidCredentialException ex) {
                System.out.println(ex.getMessage());
                continue;
            }
            // Create user
            try {
                UserRegisterCommand userRegisterCommand = new UserRegisterCommand();
                JCommander.newBuilder()
                        .addObject(userRegisterCommand)
                        .build()
                        .parse(userRegisterCommand.removePrefix(command).split(" "));
                LoginMenuController.register(userRegisterCommand.getUsername(), userRegisterCommand.getPassword(), userRegisterCommand.getNickname());
                System.out.println("user created successfully!");
                continue;
            } catch (InvalidCommandException | ParameterException ignored) {
            } catch (UsernameExistsException | NicknameExistsException ex) {
                System.out.println(ex.getMessage());
                continue;
            }
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    /**
     * In this menu, this function does nothing
     *
     * @param menu Have no effect
     */
    @Override
    public void enterMenu(MenuNames menu) {
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
