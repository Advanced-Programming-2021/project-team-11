package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.menucontrollers.DeckMenuController;
import model.User;
import model.cards.Card;
import model.cards.CardType;
import model.exceptions.*;
import view.menus.commands.deck.*;

import java.util.ArrayList;

public class DeckMenu extends Menu {
    private final User loggedInUser;

    DeckMenu(User loggedInUser) {
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
            // Try other commands
            if (addDeck(command) || setActiveDeck(command) || deleteDeck(command) || addCardToDeck(command)
                    || removeCardFromDeck(command) || showDeckCards(command))
                continue;
            if (command.equals("deck show --all")) {
                showAllDecks();
                continue;
            }
            if (command.equals("deck show --cards")) {
                showAllCards();
                continue;
            }
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    private boolean addDeck(String command) {
        try {
            DeckMenuController.addDeck(loggedInUser, new DeckCreateCommand().removePrefix(command));
            System.out.println("deck created successfully!");
            return true;
        } catch (DeckExistsException e) {
            System.out.println(e.getMessage());
            return true;
        } catch (InvalidCommandException ignored) {
            return false;
        }
    }

    private boolean setActiveDeck(String command) {
        try {
            DeckMenuController.setActiveDeck(loggedInUser, new DeckSetActiveCommand().removePrefix(command));
            System.out.println("deck activated successfully");
            return true;
        } catch (DeckDoesNotExistsException e) {
            System.out.println(e.getMessage());
            return true;
        } catch (InvalidCommandException e) {
            return false;
        }
    }

    private boolean deleteDeck(String command) {
        try {
            DeckMenuController.deleteDeck(loggedInUser, new DeckDeleteCommand().removePrefix(command));
            System.out.println("deck deleted successfully");
            return true;
        } catch (DeckDoesNotExistsException e) {
            System.out.println(e.getMessage());
            return true;
        } catch (InvalidCommandException e) {
            return false;
        }
    }

    private boolean addCardToDeck(String command) {
        try {
            DeckAddCardCommand addCardCommand = new DeckAddCardCommand();
            JCommander.newBuilder()
                    .addObject(addCardCommand)
                    .build()
                    .parse(addCardCommand.removePrefix(command).split(" "));
            DeckMenuController.addCardToDeck(loggedInUser, addCardCommand.getDeckName(), addCardCommand.getCardName(), addCardCommand.isSide());
            System.out.println("card added to deck successfully");
            return true;
        } catch (CardNotExistsException | DeckDoesNotExistsException | DeckSideOrMainFullException | DeckHaveThreeCardsException e) {
            System.out.println(e.getMessage());
            return true;
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        }
    }

    private boolean removeCardFromDeck(String command) {
        try {
            DeckRemoveCardCommand removeCardCommand = new DeckRemoveCardCommand();
            JCommander.newBuilder()
                    .addObject(removeCardCommand)
                    .build()
                    .parse(removeCardCommand.removePrefix(command).split(" "));
            DeckMenuController.removeCardFromDeck(loggedInUser, removeCardCommand.getDeckName(), removeCardCommand.getCardName(), removeCardCommand.isSide());
            System.out.println("card removed form deck successfully");
            return true;
        } catch (DeckCardNotExistsException | DeckDoesNotExistsException e) {
            System.out.println(e.getMessage());
            return true;
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        }
    }

    private void showAllDecks() {
        DeckMenuController.GetDecksResult result = DeckMenuController.getDecks(loggedInUser);
        System.out.println("Decks:");
        System.out.println("Active deck:");
        if (result.getActiveDeck() != null)
            System.out.println(result.getActiveDeck().toString());
        System.out.println("Other decks:");
        for (DeckMenuController.GetDecksResult.DeckResult deck : result.getOtherDecks())
            System.out.println(deck.toString());
    }

    private boolean showDeckCards(String command) {
        try {
            DeckShowCommand showDeckCommand = new DeckShowCommand();
            JCommander.newBuilder()
                    .addObject(showDeckCommand)
                    .build()
                    .parse(showDeckCommand.removePrefix(command).split(" "));
            ArrayList<Card> cards = DeckMenuController.getDeckCards(loggedInUser, showDeckCommand.getDeckName(), showDeckCommand.isSide());
            System.out.printf("Deck: %s\n", showDeckCommand.getDeckName());
            System.out.printf("%s deck:\n", showDeckCommand.isSide() ? "Side" : "Main");
            System.out.println("Monsters:");
            cards.stream().filter(x -> x.getCardType() == CardType.MONSTER).sorted()
                    .forEach(x -> System.out.printf("%s: %s\n", x.getName(), x.getDescription()));
            System.out.println("Spell and Traps:");
            cards.stream().filter(x -> x.getCardType() != CardType.MONSTER).sorted()
                    .forEach(x -> System.out.printf("%s: %s\n", x.getName(), x.getDescription()));
            return true;
        } catch (DeckDoesNotExistsException e) {
            System.out.println(e.getMessage());
            return true;
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        }
    }

    private void showAllCards() {
        DeckMenuController.getAllCards(loggedInUser).stream().sorted()
                .forEach(x -> System.out.printf("%s:%s\n", x.getName(), x.getDescription()));
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Deck Menu");
    }
}
