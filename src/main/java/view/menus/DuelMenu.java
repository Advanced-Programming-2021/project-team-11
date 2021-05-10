package view.menus;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import controller.GameController;
import model.PlayableCard;
import model.PlayerBoard;
import model.User;
import model.cards.monsters.BeastKingBarbaros;
import model.cards.monsters.ManEaterBug;
import model.cards.monsters.ScannerCard;
import model.enums.GamePhase;
import model.enums.GameRounds;
import model.enums.GameStatus;
import model.exceptions.*;
import model.game.GameEndResults;
import model.results.MonsterAttackResult;
import view.menus.commands.game.SelectCommand;
import view.menus.commands.game.SetCommand;

import java.util.ArrayList;
import java.util.TreeSet;

public class DuelMenu extends Menu {
    private static final String SUMMON_COMMAND = "summon", FLIP_SUMMON_COMMAND = "flip-summon",
            ATTACK_PREFIX_COMMAND = "attack ", ATTACK_DIRECT_COMMAND = "attack direct",
            ACTIVATE_EFFECT_COMMAND = "activate effect", SHOW_CARD_COMMAND = "card show --selected",
            SURRENDER_COMMAND = "surrender", CHEAT_HP = "PAINKILLER", NEXT_PHASE_COMMAND = "next phase",
            SHOW_GRAVEYARD_COMMAND = "show graveyard", PRINT_BOARD_COMMAND = "print board";
    private final GameController gameController;
    private final User player1, player2;
    private boolean isRoundEnded = false, isGameEnded = false;

    DuelMenu(User player1, User player2, GameRounds rounds) {
        this.player1 = player1;
        this.player2 = player2;
        System.out.printf("%s is the beginner!\n", player1.getUsername());
        gameController = new GameController(player1, player2, rounds);
        System.out.printf("new card added to the hand: %s\n", gameController.getRound().getPlayerBoard().getHand().get(5).getCard().getName());
        openMenu();
    }

    @Override
    void openMenu() {
        while (true) {
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
            String command = MenuUtils.readLine();
            try {
                if (processMenuCommands(command))
                    System.out.println(MenuUtils.MENU_NAV_FAILED); // THERE IS NO WAY OUT!!!!
                continue;
            } catch (InvalidCommandException ignored) {
            }
            // Check commands
            if (painkiller(command) || selectCommandProcessor(command) || nextPhase(command) || selectCommandProcessor(command)
                    || summon(command) || setCard(command) || flipSummon(command) || directAttack(command) || attackToMonster(command)
                    || showGraveyard(command) || surrender(command) || showCard(command) || printBoard(command) || activateSpell(command))
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

    private boolean printBoard(String command) {
        if (!command.equals(PRINT_BOARD_COMMAND))
            return false;
        printBoard();
        return true;
    }

    private void printBoard() {
        PlayerBoard rivalBoard = gameController.getRound().getRivalBoard();
        System.out.printf("%s:%d\n", rivalBoard.getPlayer().getUser().getNickname(), rivalBoard.getPlayer().getHealth());
        DuelMenuUtils.printRivalBoard(rivalBoard);
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
        PlayerBoard myBoard = gameController.getRound().getPlayerBoard();
        DuelMenuUtils.printMyBoard(myBoard);
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
            int index = selectCommand.getIndex();
            gameController.getRound().selectCard(index, selectCommand.isOpponent(), selectCommand.getCardPlaceType());
            System.out.println("card selected");
        } catch (InvalidCommandException | ParameterException e) {
            return false;
        } catch (NoCardSelectedYetException | NoCardFoundInPositionException e) {
            System.out.println(e.getMessage());
        }
        return true;
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
        } catch (AlreadySummonedException | NoCardSelectedYetException | CantSummonCardException
                | InvalidPhaseActionException | MonsterCardZoneFullException e) {
            System.out.println(e.getMessage());
        } catch (TributeNeededException e) {
            if (e.getCard() instanceof BeastKingBarbaros)
                success = CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound());
            else
                success = summonWithTribute(e.getNeededTributes());
        } catch (NotEnoughCardsToTributeException e) {
            if (e.getCard() instanceof BeastKingBarbaros)
                success = CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound());
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
        ArrayList<Integer> cards = DuelMenuUtils.readCardsToTribute(neededCardsToTribute);
        if (cards == null)
            return false;
        // Try to tribute
        try {
            gameController.getRound().summonCard(cards);
        } catch (NoMonsterOnTheseAddressesException e) {
            return false;
        }
        return true;
    }

    private boolean setCard(String command) {
        try {
            if (command.equals("set")) {
                handleSetSpellMonsterCard();
                return true;
            }
            SetCommand setCommand = new SetCommand();
            JCommander.newBuilder()
                    .addObject(setCommand)
                    .build()
                    .parse(setCommand.removePrefix(command + " ").split(" "));
            if (!setCommand.isValid())
                throw new InvalidCommandException();
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
            MonsterAttackResult result = gameController.getRound().attackToMonster(positionToAttack);
            System.out.println(result.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private boolean showGraveyard(String command) {
        if (!command.equals(SHOW_GRAVEYARD_COMMAND))
            return false;
        DuelMenuUtils.printGraveyard(gameController.getRound().getPlayer1Board().getGraveyard(), player1);
        DuelMenuUtils.printGraveyard(gameController.getRound().getPlayer2Board().getGraveyard(), player2);
        return true;
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

    private boolean activateSpell(String command) {
        if (!command.equals(ACTIVATE_EFFECT_COMMAND))
            return false;
        try {
            gameController.getRound().activeSpell();
        } catch (OnlySpellCardsAllowedException | NoCardSelectedException | CardAlreadyAttackedException | InvalidPhaseActionException e) {
            System.out.println(e.getMessage());
        } catch (MonsterEffectMustBeHandledException e) {
            handleMonsterWithEffectCard(e.getCard());
        }
        return true;
    }

    private void handleMonsterWithEffectCard(PlayableCard card) {
        if (card.getCard() instanceof ScannerCard) {
            try {
                CardSpecificMenus.handleScannerCardEffect(gameController.getRound().getRivalBoard().getGraveyard(), card);
            } catch (CantActivateSpellException e) {
                System.out.println(e.getMessage());
            }
        }
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
