package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.GameController;
import controller.menucontrollers.LoginMenuController;
import model.PlayerBoard;
import model.User;
import model.enums.GameRounds;
import model.exceptions.InvalidCommandException;
import model.exceptions.NoCardFoundInPositionException;
import model.exceptions.NoCardSelectedYetException;
import view.menus.commands.game.SelectCommand;

import java.util.Arrays;

public class DuelMenu extends Menu {
    private static final int[] RIVAL_BOARD_INDEXES = {5, 3, 1, 2, 4}, MY_BOARD_INDEXES = {4, 2, 1, 3, 5};
    private static final String SUMMON_COMMAND = "summon", FLIP_SUMMON_COMMAND = "flip-summon",
            ATTACK_PREFIX_COMMAND = "attack ", ATTACK_DIRECT_COMMAND = "attack direct",
            ACTIVATE_EFFECT_COMMAND = "activate effect", SHOW_CARD_COMMAND = "card show --selected",
            SURRENDER_COMMAND = "surrender", CHEAT_HP = "PAINKILLER";
    private final User player1, player2;
    private final GameController gameController;

    DuelMenu(User player1, User player2, GameRounds rounds) {
        this.player1 = player1;
        this.player2 = player2;
        // Say who is the beginner
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
            // Check commands
        }
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
            return true;
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        } catch (NoCardSelectedYetException | NoCardFoundInPositionException e) {
            System.out.println(e.getMessage());
            return true;
        }
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

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Duel Menu");
    }
}
