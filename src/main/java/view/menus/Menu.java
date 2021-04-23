package view.menus;

import model.exceptions.InvalidCommandException;

public abstract class Menu {
    abstract void openMenu();

    abstract void enterMenu(MenuNames menu);

    abstract void printMenu();

    /**
     * Process commands related to menu navigation
     *
     * @return True if this is back menu
     * @throws InvalidCommandException If the command is not about menu navigation
     */
    protected final boolean processMenuCommands(String command) throws InvalidCommandException {
        if (command.equals("menu exit"))
            return true;
        if (command.equals("menu show-current")) {
            printMenu();
            return false;
        }
        if (command.startsWith("menu enter ")) {
            MenuNames menuName = MenuNames.parseMenuName(command.substring("menu enter ".length()));
            if (menuName == MenuNames.INVALID)
                throw new InvalidCommandException();
            enterMenu(menuName);
            return false;
        }
        throw new InvalidCommandException();
    }
}
