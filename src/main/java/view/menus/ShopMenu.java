package view.menus;

import controller.menucontrollers.ShopMenuController;
import model.User;
import model.cards.Card;
import model.exceptions.CardNotExistsException;
import model.exceptions.InsufficientBalanceException;
import model.exceptions.InvalidCommandException;
import view.menus.commands.shop.ShopBuyItemCommand;

public class ShopMenu extends Menu {
    private final User loggedInUser;

    ShopMenu(User loggedInUser) {
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
            // Other commands
            if (buyCard(command) || hesoyam(command) || showAllCards(command))
                continue;
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    private boolean buyCard(String command) {
        try {
            String cardName = new ShopBuyItemCommand().removePrefix(command);
            ShopMenuController.buyCardForUser(loggedInUser, cardName);
            return true;
        } catch (InvalidCommandException e) {
            return false;
        } catch (InsufficientBalanceException | CardNotExistsException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    private boolean showAllCards(String command) {
        if (command.equals("shop show --all")) {
            Card.getAllCards().forEach(x -> System.out.printf("%s:%d\n", x.getName(), x.getPrice()));
            return true;
        }
        return false;
    }

    private boolean hesoyam(String command) {
        if (command.equals("HESOYAM")) {
            ShopMenuController.increaseMoneyCheat(loggedInUser);
            System.out.println("$$$$$$$$$$$$$$$$");
            return true;
        }
        return false;
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Shop Menu");
    }
}