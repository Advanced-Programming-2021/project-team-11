package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.menucontrollers.DeckMenuController;
import model.User;
import model.exceptions.DeckCardNotExistsException;
import model.exceptions.InvalidCommandException;
import view.menus.commands.CommandUtils;
import view.menus.commands.deck.DeckSwapCardCommand;

public class DuelChangeSideDeckMenu extends Menu {
    private final static String SHOW_DECK_COMMAND = "show deck";
    private final User toChangeUser;

    DuelChangeSideDeckMenu(User toChangeUser) {
        this.toChangeUser = toChangeUser;
        System.out.printf("%s you can customize your deck now! Use \"menu exit\" to exit\n", toChangeUser.getNickname());
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
            if (showDeck(command) || swapCards(command))
                continue;
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    private boolean showDeck(String command) {
        if (!command.equals(SHOW_DECK_COMMAND))
            return false;
        DeckMenu.showDeckCards(toChangeUser.getActiveDeck().getMainDeck(), false);
        DeckMenu.showDeckCards(toChangeUser.getActiveDeck().getSideDeck(), true);
        return true;
    }

    private boolean swapCards(String command) {
        try {
            DeckSwapCardCommand swapCardCommand = new DeckSwapCardCommand();
            JCommander.newBuilder()
                    .addObject(swapCardCommand)
                    .build()
                    .parse(CommandUtils.translateCommandline(swapCardCommand.removePrefix(command)));
            DeckMenuController.swapCards(toChangeUser.getActiveDeck(), swapCardCommand.getMainDeckCardName(), swapCardCommand.getSideDeckCardName());
            System.out.println("Cards swapped!");
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        } catch (DeckCardNotExistsException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Deck change menu (mid game)");
    }
}
