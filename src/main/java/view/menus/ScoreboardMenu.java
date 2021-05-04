package view.menus;

import controller.menucontrollers.ScoreboardMenuController;
import model.exceptions.InvalidCommandException;

public class ScoreboardMenu extends Menu {
    ScoreboardMenu() {
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
            if (command.equals("scoreboard show")) {
                showScoreboard();
                continue;
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
        System.out.println("Scoreboard Menu");
    }

    private void showScoreboard() {
        ScoreboardMenuController.getScoreboardLines().forEach(System.out::println);
    }
}
