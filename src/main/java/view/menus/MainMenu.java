package view.menus;

import model.User;
import model.exceptions.InvalidCommandException;

public class MainMenu extends Menu {
    private final User loggedInUser;

    public MainMenu(User loggedInUser) {
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
            if (command.equals("logout"))
                return;
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    @Override
    void enterMenu(MenuNames menu) {
        switch (menu) {
            case MAIN:
                System.out.println(MenuUtils.MENU_NAV_FAILED);
                break;
            case LOGIN:
                System.out.println("please use logout command");
                break;
            case DUEL:
                System.out.println("please use the duel command");
                break;
            case SCOREBOARD:
                new ScoreboardMenu();
                break;
            case PROFILE:
                new ProfileMenu(loggedInUser);
                break;
            case DECK:
                new DeckMenu(loggedInUser);
                break;
            case SHOP:
                new ShopMenu(loggedInUser);
                break;
        }
    }

    @Override
    void printMenu() {
        System.out.println("Main Menu");
    }
}
