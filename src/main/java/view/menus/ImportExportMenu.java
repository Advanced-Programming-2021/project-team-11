package view.menus;

import model.exceptions.InvalidCommandException;

public class ImportExportMenu extends Menu {
    ImportExportMenu() {
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
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Import/Export Menu");
    }
}
