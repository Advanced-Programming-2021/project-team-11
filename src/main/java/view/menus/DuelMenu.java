package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.GameController;
import controller.GameRoundController;
import controller.menucontrollers.LoginMenuController;
import model.PlayerBoard;
import model.User;
import model.enums.GamePhase;
import model.enums.GameRounds;
import model.enums.GameStatus;
import model.exceptions.*;
import model.game.GameEndResults;
import view.menus.commands.game.SelectCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class DuelMenu extends Menu {
    private static final int[] RIVAL_BOARD_INDEXES = {5, 3, 1, 2, 4}, MY_BOARD_INDEXES = {4, 2, 1, 3, 5};
    private static final String SUMMON_COMMAND = "summon", FLIP_SUMMON_COMMAND = "flip-summon",
            ATTACK_PREFIX_COMMAND = "attack ", ATTACK_DIRECT_COMMAND = "attack direct",
            ACTIVATE_EFFECT_COMMAND = "activate effect", SHOW_CARD_COMMAND = "card show --selected",
            SURRENDER_COMMAND = "surrender", CHEAT_HP = "PAINKILLER", NEXT_PHASE_COMMAND = "next phase",
            CANCEL_COMMAND = "cancel";
    private final GameController gameController;
    private final User player1, player2;
    private boolean isRoundEnded = false, isGameEnded = false;

    DuelMenu(User player1, User player2, GameRounds rounds) {
        this.player1 = player1;
        this.player2 = player2;
        System.out.printf("%s is the beginner!", player1.getUsername());
        gameController = new GameController(player1, player2, rounds);
        openMenu();
    }

    @Override
    void openMenu() {
        while (true) {
            String command = MenuUtils.readLine();
            try {
                if (processMenuCommands(command))
                    System.out.println(MenuUtils.MENU_NAV_FAILED); // THERE IS NO WAY OUT!!!!
                continue;
            } catch (InvalidCommandException ignored) {
            }
            // Check game and round status
            if (isGameEnded) {
                System.out.println("Game Over!");
                return;
            }
            if (isRoundEnded) { // when we reach here, we must allow the users to change their decks
                isRoundEnded = false;
                new DuelChangeSideDeckMenu(player1).openMenu();
                new DuelChangeSideDeckMenu(player2).openMenu();
                // TODO Start a new round
                continue;
            }
            // Check commands
            if (painkiller(command) || selectCommandProcessor(command) || nextPhase(command) || selectCommandProcessor(command) || summon(command))
                continue;
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    /**
     * Checks the round end status and does some stuff in controller in order to make
     *
     * @return True if the round has been ended
     */
    private boolean checkRoundEnd() {
        if (gameController.isRoundEnded() != GameStatus.ONGOING) {
            isRoundEnded = true;
            GameEndResults results = gameController.isGameEnded();
            if (results != null) {
                isGameEnded = true;
                // Apply the results
                System.out.printf("%s is the winner!\n", results.didPlayer1Won() ? player1.getNickname() : player2.getNickname());
                player1.increaseScore(results.getPlayer1Score());
                player1.increaseMoney(results.getPlayer1Money());
                player2.increaseScore(results.getPlayer2Score());
                player2.increaseMoney(results.getPlayer2Money());
            }
            return true;
        }
        return false;
    }

    private void printBoard() {
        PlayerBoard rivalBoard = gameController.getRound().getRivalBoard();
        System.out.printf("%s:%d\n", rivalBoard.getPlayer().getUser().getNickname(), rivalBoard.getPlayer().getHealth());
        printRivalBoard(rivalBoard);
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
        PlayerBoard myBoard = gameController.getRound().getPlayerBoard();
        System.out.printf("%s:%d\n", myBoard.getPlayer().getUser().getNickname(), myBoard.getPlayer().getHealth());
    }

    private boolean painkiller(String command) {
        if (command.equals(CHEAT_HP)) {
            gameController.getRound().painkiller();
            return true;
        }
        return false;
    }

    private boolean selectCommandProcessor(String command) {
        try {
            SelectCommand selectCommand = new SelectCommand();
            JCommander.newBuilder()
                    .addObject(selectCommand)
                    .build()
                    .parse(selectCommand.removePrefix(command).split(" "));
            if (!selectCommand.isValid())
                throw new InvalidCommandException();
            // Check what command this is
            if (selectCommand.isDeselect()) {
                gameController.getRound().deselectCard();
                System.out.println("card deselected");
                return true;
            }
            if (!selectCommand.isSelectionValid()) {
                System.out.println("invalid selection");
                return true;
            }
            gameController.getRound().selectCard(selectCommand.getIndex(), selectCommand.isOpponent(), selectCommand.getCardPlaceType());
            System.out.println("card selected");
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        } catch (NoCardSelectedYetException | NoCardFoundInPositionException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static void printRivalBoard(PlayerBoard board) {
        // Count cards in hand
        board.getHand().forEach(x -> System.out.print("\tc"));
        System.out.println();
        // Print deck
        System.out.printf("%02d\n", board.getDeck().size());
        // Spell and traps
        Arrays.stream(RIVAL_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getSpellCards()[i - 1] == null ? "E " : board.getSpellCards()[i - 1].toRivalString())));
        System.out.println();
        Arrays.stream(RIVAL_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getMonsterCards()[i - 1] == null ? "E " : board.getMonsterCards()[i - 1].toRivalString())));
        System.out.println();
        // Graveyard and field
        System.out.printf("%02d", board.getGraveyard().size());
        for (int i = 0; i < 6; i++)
            System.out.print('\t');
        System.out.println("E");
    }

    public static void printMyBoard(PlayerBoard board) {
        // Graveyard and field
        System.out.printf("%02d", board.getGraveyard().size());
        for (int i = 0; i < 6; i++)
            System.out.print('\t');
        System.out.println("E");
        // Spell and traps
        Arrays.stream(MY_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getMonsterCards()[i - 1] == null ? "E " : board.getMonsterCards()[i - 1].toRivalString())));
        System.out.println();
        Arrays.stream(RIVAL_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getSpellCards()[i - 1] == null ? "E " : board.getSpellCards()[i - 1].toRivalString())));
        System.out.println();
        // Print deck
        for (int i = 0; i < 6; i++)
            System.out.print('\t');
        System.out.printf("%02d\n", board.getDeck().size());
        // Count cards in hand
        board.getHand().forEach(x -> System.out.print("c\t"));
        System.out.println();
    }

    private boolean nextPhase(String command) {
        if (!command.equals(NEXT_PHASE_COMMAND))
            return false;
        int oldHandSize = gameController.getRound().getPlayerBoard().getHand().size();
        GamePhase nowPhase = gameController.getRound().advancePhase();
        if (checkRoundEnd()) // check player lost on draw
            return true;
        // Otherwise process the data
        System.out.printf("phase: %s\n", nowPhase.toString());
        switch (nowPhase) {
            case DRAW:
                if (oldHandSize != 6)
                    System.out.printf("new card added to the hand: %s\n", gameController.getRound().getPlayerBoard().getHand().get(oldHandSize).getCard().getName());
                break;
            case MAIN1:
            case MAIN2:
                printBoard();
                break;
            case END_PHASE:
                System.out.printf("its %s's turn\n", gameController.getRound().getPlayerBoard().getPlayer().getUser().getNickname());
                break;
        }
        return true;
    }

    private boolean summon(String command) {
        if (!command.equals(SUMMON_COMMAND))
            return false;
        boolean success = false;
        try {
            gameController.getRound().summonCard();
            success = true;
        } catch (AlreadySummonedException | NotEnoughCardsToTributeException | NoCardSelectedYetException | CantSummonCardException
                | InvalidPhaseActionException | MonsterCardZoneFullException e) {
            System.out.println(e.getMessage());
        } catch (TributeNeededException e) {
            success = summonWithTribute(e.getNeededTributes());
        }
        if (success) {
            System.out.println("summoned successfully");
            printBoard();
        }
        return true;
    }

    /**
     * Gets the card positions to remove from user monster card zone
     *
     * @param neededCardsToTribute Number of cards needed to tribute
     * @return True if we have successfully got all card. False if user have canceled it
     */
    private boolean summonWithTribute(int neededCardsToTribute) {
        TreeSet<Integer> cardPositions = new TreeSet<>();
        while (cardPositions.size() != neededCardsToTribute) {
            System.out.print("Select a card position to tribute or type \"cancel\" to cancel: ");
            String command = MenuUtils.readLine();
            if (command.equals(CANCEL_COMMAND))
                return false;
            try {
                cardPositions.add(Integer.parseInt(command));
            } catch (NumberFormatException ignored) {
            }
        }
        // Try to tribute
        try {
            gameController.getRound().summonCard(new ArrayList<>(cardPositions));
        } catch (NoMonsterOnTheseAddressesException e) {
            return false;
        }
        return true;
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
