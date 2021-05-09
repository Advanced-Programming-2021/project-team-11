package view.menus;

import com.beust.jcommander.JCommander;
import controller.GameUtils;
import model.Deck;
import model.User;
import model.enums.GameRounds;
import model.exceptions.*;
import view.menus.commands.duelstart.DuelStartCommand;

public class DuelStartMenu extends Menu {
    private final User player1;

    DuelStartMenu(User player1) {
        this.player1 = player1;
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
            // Here we should start a duel. Read the command until we read the start
            if (!startRound(command))
                System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    private boolean startRound(String command) {
        try {
            DuelStartCommand duelStartCommand = new DuelStartCommand();
            JCommander.newBuilder()
                    .addObject(duelStartCommand)
                    .build()
                    .parse(duelStartCommand.removePrefix(command).split(" "));
            if (!duelStartCommand.isValid())
                return false;
            if (duelStartCommand.getRounds() != 3 && duelStartCommand.getRounds() != 1)
                throw new InvalidRoundNumbersException();
            if (duelStartCommand.isAi())
                startAiDuel(duelStartCommand.getRounds());
            else
                startMultiplayerDuel(duelStartCommand.getSecondPlayerName(), duelStartCommand.getRounds());
            return true;
        } catch (InvalidCommandException e) {
            return false;
        } catch (InvalidRoundNumbersException | UsernameNotExistsException | UserHaveNoActiveDeckException
                | UserDeckIsInvalidException | PlayedYourselfException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    private void startMultiplayerDuel(String secondPlayerUsername, int rounds) throws UserHaveNoActiveDeckException, UsernameNotExistsException, UserDeckIsInvalidException, PlayedYourselfException {
        User player2 = User.getUserByUsername(secondPlayerUsername);
        if (player2 == null)
            throw new UsernameNotExistsException();
        if (player1 == player2)
            throw new PlayedYourselfException();
        // Check both players decks
        player1.validateUserActiveDeck();
        player2.validateUserActiveDeck();
        // Draw to get the first player
        boolean isPlayer1Starter = GameUtils.random.nextBoolean();
        new DuelMenu(isPlayer1Starter ? player1 : player2, isPlayer1Starter ? player2 : player1, rounds == 1 ? GameRounds.ONE : GameRounds.THREE);
    }

    private void startAiDuel(int rounds) {
        // TODO :|
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Duel Menu");
    }
}
