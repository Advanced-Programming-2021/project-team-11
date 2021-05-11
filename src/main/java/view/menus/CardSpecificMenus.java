package view.menus;

import controller.GameRoundController;
import model.PlayableCard;
import model.PlayerBoard;
import model.cards.CardType;
import model.cards.MonsterCard;
import model.cards.monsters.BeastKingBarbaros;
import model.cards.monsters.ManEaterBug;
import model.enums.CardPlaceType;
import model.exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CardSpecificMenus {
    public static void handleManEaterBugRemoval(PlayerBoard rivalBoard, ManEaterBug card) {
        // At first check if the rival board is empty; Can't do anything if the board is empty!
        if (rivalBoard.isMonsterZoneEmpty())
            return;
        // Now ask the user to remove one card from rival
        System.out.print("Choose an position based on rival board to remove. Choose an empty position or type \"cancel\" to cancel. ");
        int position;
        while (true) {
            try {
                position = Integer.parseInt(MenuUtils.readLine());
                if (position >= 1 && position <= 5)
                    break;
            } catch (NumberFormatException ignored) {
            }
            System.out.println(MenuUtils.INVALID_NUMBER);
        }
        position = DuelMenuUtils.inputToRivalBoard(position);
        card.activateEffect(null, rivalBoard, null, rivalBoard.getMonsterCards()[position], 0);
    }

    public static void handleScannerCardEffect(ArrayList<PlayableCard> rivalGraveyard, PlayableCard scannerCard) throws CantActivateSpellException {
        if (rivalGraveyard.stream().noneMatch(card -> card.getCard().getCardType() == CardType.MONSTER))
            throw new CantActivateSpellException();
        // Show the graveyard
        DuelMenuUtils.printGraveyard(rivalGraveyard, "rival");
        // Get the card
        int index = -1;
        while (index < 0 || index >= rivalGraveyard.size()) {
            System.out.print("Choose a card by it's number: ");
            String command = MenuUtils.readLine();
            if (command.equals(MenuUtils.CANCEL_COMMAND))
                return;
            try {
                index = Integer.parseInt(MenuUtils.readLine());
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
            if (rivalGraveyard.get(index).getCard().getCardType() != CardType.MONSTER) {
                index = -1;
                System.out.println("Please select a monster card");
            }
        }
        scannerCard.activateEffect(null, null, rivalGraveyard.get(index));
        System.out.printf("Your scanner is now %s!\n", rivalGraveyard.get(index).getCard().getName());
    }

    /**
     * Tries to summon Beast King Barbaros in one of the ways which it can spawn
     *
     * @param gameRoundController The round which is ongoing
     * @return True if successfully summoned. Otherwise canceled
     */
    public static boolean summonBeastKingBarbaros(GameRoundController gameRoundController) {
        int tributes, monstersHave = gameRoundController.getPlayerBoard().countActiveMonsterCards();
        while (true) {
            try {
                System.out.print("How many cards you want to tribute? (0/2/3) ");
                tributes = Integer.parseInt(MenuUtils.readLine());
                if (tributes != 0 && tributes != 2 && tributes != 3)
                    throw new TributeNeededException(null, 0);
                if (tributes > monstersHave)
                    throw new NotEnoughCardsToTributeException(null);
                break;
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            } catch (TributeNeededException ex) {
                System.out.println("Invalid tributes number!");
            } catch (NotEnoughCardsToTributeException ex) {
                System.out.println(ex.getMessage());
            }
        }
        ArrayList<Integer> cards = DuelMenuUtils.readCardsToTribute(tributes);
        if (cards == null)
            return false;
        if (tributes == 0)
            gameRoundController.returnPlayableCard().addAttackDelta(BeastKingBarbaros.getToReduceAttack());
        try {
            gameRoundController.summonCard(cards);
        } catch (NoMonsterOnTheseAddressesException e) {
            System.out.println(e.getMessage());
            return false;
        }
        if (tributes == 3)
            Arrays.stream(gameRoundController.getRivalBoard().getMonsterCards()).forEach(card -> gameRoundController.getRivalBoard().sendToGraveyard(card));
        return true;
    }

    /**
     * Tries to move a card from graveyard to your hand with Herald Of Creation card
     *
     * @param playerBoard The board of player who has this card
     * @param thisCard    This card
     * @throws NotEnoughCardsToTributeException There is not enough cards in hand
     * @throws NoSuitableCardFoundException     There no card which can be used from players graveyard
     */
    public static void summonCardWithHeraldOfCreation(PlayerBoard playerBoard, PlayableCard thisCard) throws NotEnoughCardsToTributeException, NoSuitableCardFoundException {
        if (playerBoard.getHand().size() == 0)
            throw new NotEnoughCardsToTributeException(null);
        if (playerBoard.getGraveyard().stream().noneMatch(card -> card.getCard().getCardType() == CardType.MONSTER && ((MonsterCard) card.getCard()).getLevel() >= 7))
            throw new NoSuitableCardFoundException();
        int handIndex, graveyardIndex;
        System.out.println("Your hand:");
        DuelMenuUtils.printNumberedCardList(playerBoard.getHand());
        while (true) {
            try {
                System.out.print("Select a card from your hand by index: ");
                String input = MenuUtils.readLine();
                if (input.equals(MenuUtils.CANCEL_COMMAND))
                    return;
                handIndex = Integer.parseInt(input);
                if (handIndex < 0 || handIndex >= playerBoard.getHand().size())
                    throw new NumberFormatException();
                break;
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
        }
        // Now show the graveyard
        DuelMenuUtils.printGraveyard(playerBoard.getGraveyard(), playerBoard.getPlayer().getUser());
        while (true) {
            try {
                System.out.print("Select a card from your graveyard by index: ");
                String input = MenuUtils.readLine();
                if (input.equals(MenuUtils.CANCEL_COMMAND))
                    return;
                graveyardIndex = Integer.parseInt(input);
                if (graveyardIndex < 0 || graveyardIndex >= playerBoard.getGraveyard().size())
                    throw new NumberFormatException();
                break;
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
        }
        // Do the thing
        playerBoard.getHand().remove(handIndex);
        PlayableCard toMoveCard = playerBoard.getGraveyard().remove(graveyardIndex);
        toMoveCard.setCardPlace(CardPlaceType.HAND);
        playerBoard.getHand().add(toMoveCard);
        thisCard.activateEffect(null, null, null); // has no effect here
        System.out.println("Card moved successfully!");
    }

    public static void handleTerratigerTheEmpoweredWarriorSummon(PlayerBoard playerBoard) {
        ArrayList<PlayableCard> allowedCards = playerBoard.getHand().stream().filter(card -> card.getCard().getCardType() == CardType.MONSTER
                && ((MonsterCard) card.getCard()).getLevel() <= 4).collect(Collectors.toCollection(ArrayList::new));
        if (allowedCards.size() == 0)
            return;
        if (playerBoard.isMonsterZoneFull())
            return;
        DuelMenuUtils.printNumberedCardList(allowedCards);
        int index;
        while (true) {
            try {
                System.out.print("Select a card from your hand by index: ");
                String input = MenuUtils.readLine();
                if (input.equals(MenuUtils.CANCEL_COMMAND))
                    return;
                index = Integer.parseInt(input);
                if (index < 0 || index >= allowedCards.size())
                    throw new NumberFormatException();
                break;
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
        }
        // Move the card
        playerBoard.removeHandCard(allowedCards.get(index));
        playerBoard.addMonsterCard(allowedCards.get(index));
        allowedCards.get(index).makeVisible();
        allowedCards.get(index).setDefencing();
        System.out.printf("%s summoned successfully!\n", allowedCards.get(index).getCard().getName());
    }

    /**
     * Tries to spawn the {@link model.cards.monsters.TheTricky} card
     * To spawn it, we need to tribute a card from player hand
     *
     * @param playerBoard The player board
     * @param thisCard    This card which we want to spawn
     * @return True if summoned successfully, otherwise false
     */
    public static boolean spawnTheTricky(PlayerBoard playerBoard, PlayableCard thisCard) {
        if (playerBoard.getHand().size() <= 1) {
            System.out.println("Your hand is empty!");
            return false;
        }
        if (playerBoard.isMonsterZoneFull()) {
            System.out.println(new MonsterCardZoneFullException().getMessage()); // SAM KHALES
            return false;
        }
        DuelMenuUtils.printNumberedCardList(playerBoard.getHand());
        int index;
        while (true) {
            try {
                index = Integer.parseInt(MenuUtils.readLine());
                if (index < 0 || index >= playerBoard.getHand().size())
                    throw new NumberFormatException();
                if (playerBoard.getHand().get(index) == thisCard)
                    System.out.println("You played yourself!");
                else
                    break;
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
        }
        // Remove the card
        playerBoard.getHand().remove(index);
        playerBoard.getHand().remove(thisCard);
        playerBoard.addMonsterCard(thisCard);
        return true;
    }
}
