package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.GameController;
import model.PlayableCard;
import model.PlayerBoard;
import model.User;
import model.cards.monsters.ManEaterBug;
import model.enums.GamePhase;
import model.enums.GameRounds;
import model.enums.GameStatus;
import model.exceptions.*;
import model.game.GameEndResults;
import model.results.MonsterAttackResult;
import view.menus.commands.game.SelectCommand;
import view.menus.commands.game.SetCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class DuelMenu extends Menu {
    private static final int[] RIVAL_BOARD_INDEXES = {5, 3, 1, 2, 4}, MY_BOARD_INDEXES = {4, 2, 1, 3, 5};
    private static final String SUMMON_COMMAND = "summon", FLIP_SUMMON_COMMAND = "flip-summon",
            ATTACK_PREFIX_COMMAND = "attack ", ATTACK_DIRECT_COMMAND = "attack direct",
            ACTIVATE_EFFECT_COMMAND = "activate effect", SHOW_CARD_COMMAND = "card show --selected",
            SURRENDER_COMMAND = "surrender", CHEAT_HP = "PAINKILLER", NEXT_PHASE_COMMAND = "next phase",
            CANCEL_COMMAND = "cancel", SHOW_GRAVEYARD_COMMAND = "show graveyard";
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
            if (checkRoundEnd())
                continue;
            // Check commands
            if (painkiller(command) || selectCommandProcessor(command) || nextPhase(command) || selectCommandProcessor(command)
                    || summon(command) || setCard(command) || flipSummon(command) || directAttack(command) || attackToMonster(command)
                    || showGraveyard(command) || surrender(command) || showCard(command))
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
        GameStatus roundStatus = gameController.isRoundEnded();
        if (roundStatus != GameStatus.ONGOING) {
            isRoundEnded = true;
            GameEndResults results = gameController.isGameEnded();
            if (results != null) {
                isGameEnded = true;
                // Apply the results
                System.out.printf("%s won the whole match with score: %d-%d\n",
                        results.didPlayer1Won() ? player1.getNickname() : player2.getNickname(),
                        player1.getScore(), player2.getScore());
                player1.increaseScore(results.getPlayer1Score());
                player1.increaseMoney(results.getPlayer1Money());
                player2.increaseScore(results.getPlayer2Score());
                player2.increaseMoney(results.getPlayer2Money());
                return true;
            }
            System.out.printf("%s won the game\n", roundStatus == GameStatus.PLAYER1_WON ? player1.getNickname() : player2.getNickname());
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
            int index = selectCommand.isOpponent() ? inputToRivalBoard(selectCommand.getIndex()) : inputToPlayerBoard(selectCommand.getIndex());
            gameController.getRound().selectCard(index, selectCommand.isOpponent(), selectCommand.getCardPlaceType());
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

    private boolean setCard(String command) {
        try {
            SetCommand setCommand = new SetCommand();
            JCommander.newBuilder()
                    .addObject(setCommand)
                    .build()
                    .parse(setCommand.removePrefix(command).split(" "));
            if (!setCommand.isValid())
                throw new InvalidCommandException();
            if (setCommand.getPosition() == null || setCommand.getPosition().equals(""))
                handleSetSpellMonsterCard();
            else
                handleChangeCardPosition(setCommand.getPosition().equals("attack"));
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        }
        return true;
    }

    /**
     * Sets a Trap/Spell/Monster card on ground
     */
    private void handleSetSpellMonsterCard() {
        try {
            gameController.getRound().setCard();
            System.out.println("set successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangeCardPosition(boolean isAttacking) {
        try {
            gameController.getRound().setCardPosition(isAttacking);
            System.out.println("monster card position changed successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean flipSummon(String command) {
        if (!command.equals(FLIP_SUMMON_COMMAND))
            return false;
        try {
            PlayableCard selectedCard = gameController.getRound().returnPlayableCard();
            gameController.getRound().flipSummon();
            if (selectedCard.getCard() instanceof ManEaterBug)
                CardSpecificMenus.handleManEaterBugRemoval(gameController.getRound().getRivalBoard(), (ManEaterBug) selectedCard.getCard());
            System.out.println("flip summoned successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private boolean directAttack(String command) {
        if (!command.equals(ATTACK_DIRECT_COMMAND))
            return false;
        try {
            int damageReceived = gameController.getRound().attackToPlayer();
            System.out.printf("you opponent receives %d battle damage\n", damageReceived);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private boolean attackToMonster(String command) {
        if (!command.startsWith(ATTACK_PREFIX_COMMAND))
            return false;
        int positionToAttack;
        try {
            positionToAttack = Integer.parseInt(command.substring(ATTACK_PREFIX_COMMAND.length()));
            if (positionToAttack <= 0 || positionToAttack > 5)
                throw new InvalidCommandException();
        } catch (InvalidCommandException | NumberFormatException e) {
            return false;
        }
        try {
            MonsterAttackResult result = gameController.getRound().attackToMonster(inputToRivalBoard(positionToAttack));
            System.out.println(result.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private boolean showGraveyard(String command) {
        if (!command.equals(SHOW_GRAVEYARD_COMMAND))
            return false;
        System.out.printf("%s's graveyard\n", player1.getNickname());
        printGraveyard(gameController.getRound().getPlayer1Board().getGraveyard());
        System.out.printf("%s's graveyard\n", player2.getNickname());
        printGraveyard(gameController.getRound().getPlayer2Board().getGraveyard());
        return true;
    }

    private static void printGraveyard(ArrayList<PlayableCard> graveyard) {
        if (graveyard.size() == 0)
            System.out.println("graveyard empty");
        else
            for (int i = 0; i < graveyard.size(); i++)
                System.out.printf("%d. %s:%s\n", i + 1, graveyard.get(i).getCard().getName(), graveyard.get(i).getCard().getDescription());
    }

    private boolean surrender(String command) {
        if (!command.equals(SURRENDER_COMMAND))
            return false;
        gameController.getRound().surrender();
        return true;
    }

    private boolean showCard(String command) {
        if (!command.equals(SHOW_CARD_COMMAND))
            return false;
        try {
            System.out.println(gameController.getRound().getSelectedCard());
        } catch (NoCardSelectedYetException | CardHiddenException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static int inputToPlayerBoard(int index) {
        for (int i = 0; i < MY_BOARD_INDEXES.length; i++)
            if (MY_BOARD_INDEXES[i] == index)
                return i;
        throw new BooAnException("Invalid input: " + index);
    }

    public static int inputToRivalBoard(int index) {
        for (int i = 0; i < RIVAL_BOARD_INDEXES.length; i++)
            if (RIVAL_BOARD_INDEXES[i] == index)
                return i;
        throw new BooAnException("Invalid input: " + index);
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
