package view.menus;

import controller.GameRoundController;
import controller.GameUtils;
import model.PlayableCard;
import model.PlayerBoard;
import model.cards.*;
import model.cards.monsters.BeastKingBarbaros;
import model.cards.monsters.ManEaterBug;
import model.cards.spells.AdvancedRitualArt;
import model.cards.spells.EquipSpellCard;
import model.cards.traps.MindCrush;
import model.cards.traps.NegateAttack;
import model.enums.CardPlaceType;
import model.exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CardSpecificMenus2 {
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
            System.out.print(MenuUtils.CHOOSE_CARD_BY_INDEX);
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
            gameRoundController.returnSelectedCard().addAttackDelta(BeastKingBarbaros.getToReduceAttack());
        try {
            gameRoundController.summonCard(cards, true);
        } catch (NoMonsterOnTheseAddressesException | TrapCanBeActivatedException e) {
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
     */
    public static void summonCardWithHeraldOfCreation(PlayerBoard playerBoard, PlayableCard thisCard) {
        int handIndex, graveyardIndex;
        System.out.println("Your hand:");
        DuelMenuUtils.printNumberedCardList(playerBoard.getHand());
        while (true) {
            try {
                System.out.print(MenuUtils.CHOOSE_CARD_BY_INDEX);
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
                System.out.print(MenuUtils.CHOOSE_CARD_BY_INDEX);
                String input = MenuUtils.readLine();
                if (input.equals(MenuUtils.CANCEL_COMMAND))
                    return;
                graveyardIndex = Integer.parseInt(input) - 1;
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
                System.out.print(MenuUtils.CHOOSE_CARD_BY_INDEX);
                String input = MenuUtils.readLine();
                if (input.equals(MenuUtils.CANCEL_COMMAND))
                    return;
                index = Integer.parseInt(input) - 1;
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
                index = Integer.parseInt(MenuUtils.readLine()) - 1;
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

    public static void handleRitualSpawn(PlayerBoard board, PlayableCard spellCard) {
        // At first print the monsters which user can choose
        ArrayList<PlayableCard> list = DuelMenuUtils.printNumberedCardList(board.getHand().stream().filter
                (card -> card.getCard() instanceof RitualMonster));
        int index;
        while (true) {
            System.out.print(MenuUtils.CHOOSE_CARD_BY_INDEX);
            try {
                index = Integer.parseInt(MenuUtils.readLine()) - 1;
                if (index < 0 || index >= list.size())
                    throw new NumberFormatException();
                if (AdvancedRitualArt.getInstance().isConditionMade(board, null, list.get(index), 0))
                    break;
                else
                    System.out.println("You can't ritual summon this card!");
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
        }
        // Choose cards which it levels match
        DuelMenuUtils.printNumberedCardListWithLevel(board.getMonsterCardsList());
        TreeSet<Integer> indexesOfMonsters = new TreeSet<>();
        while (true) {
            System.out.print("Choose some monsters by their index, separated by ',': ");
            try {
                for (String number : MenuUtils.readLine().split(","))
                    indexesOfMonsters.add(Integer.parseInt(number));
                // Check level
                int sum = indexesOfMonsters.stream().mapToInt(i -> ((MonsterCard) board.getSpellCardsList().get(i).getCard()).getLevel()).sum();
                if (((RitualMonster) list.get(index).getCard()).getLevel() == sum)
                    break;
                else
                    System.out.printf("The sum of levels of card which you have chosen is not %d!\n", ((RitualMonster) list.get(index).getCard()).getLevel());
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
            indexesOfMonsters.clear();
        }
        // Tribute and summon
        board.sendToGraveyard(spellCard);
        ArrayList<PlayableCard> monstersToTribute = new ArrayList<>();
        indexesOfMonsters.forEach(i -> monstersToTribute.add(board.getMonsterCardsList().get(i)));
        monstersToTribute.forEach(board::sendToGraveyard);
        board.addMonsterCard(list.get(index));
    }

    public static void handleMonsterReborn(GameRoundController gameRoundController, PlayableCard thisCard) {
        // Get the graveyards, mix them and then show them
        ArrayList<PlayableCard> cards = new ArrayList<>();
        cards.addAll(gameRoundController.getPlayerBoard().getGraveyard());
        cards.addAll(gameRoundController.getRivalBoard().getGraveyard());
        DuelMenuUtils.printNumberedCardList(cards);
        // Let the player choose a card
        int index = MenuUtils.readCardByIndex(cards.size());
        // Summon that card
        gameRoundController.specialSummon((MonsterCard) cards.get(index).getCard(), true);
        gameRoundController.getPlayerBoard().sendToGraveyard(thisCard);
        System.out.println(cards.get(index).getCard().getName() + " summoned!");
    }

    public static void handleTerraforming(PlayerBoard board, PlayableCard thisCard) {
        ArrayList<Card> list = DuelMenuUtils.printNumberedRawCardList(board.getDeck().stream()
                .filter(card -> card instanceof SpellCard && ((SpellCard) card).getSpellCardType() == SpellCardType.FIELD));
        int index = MenuUtils.readCardByIndex(list.size());
        if (index == -1)
            return;
        // Remove that card from hand
        board.getDeck().remove(list.get(index));
        board.getHand().add(new PlayableCard(list.get(index), CardPlaceType.HAND));
        board.shuffleDeck();
        board.removeHandCard(thisCard);
    }

    public static void handleChangeOfHeart(GameRoundController roundController, PlayableCard thisCard) {
        ArrayList<PlayableCard> monsters = roundController.getRivalBoard().getMonsterCardsList();
        DuelMenuUtils.printNumberedCardList(monsters);
        int index = MenuUtils.readCardByIndex(monsters.size());
        if (index == -1)
            return;
        roundController.changeOfHeartSwapOwner(monsters.get(index));
        roundController.getPlayerBoard().removeHandCard(thisCard);
    }

    public static void equip(GameRoundController roundController, PlayableCard thisCard) {
        ArrayList<PlayableCard> monsters = roundController.getPlayerBoard().getMonsterCardsList();
        DuelMenuUtils.printNumberedCardList(monsters);
        int index = MenuUtils.readCardByIndex(monsters.size());
        if (index == -1)
            return;
        monsters.get(index).setEquippedCard((EquipSpellCard) thisCard.getCard());
    }

    public static void callOfTheHunted(PlayerBoard board, PlayableCard thisCard) {
        ArrayList<PlayableCard> monsters = DuelMenuUtils.printNumberedCardList(board.getGraveyard().stream()
                .filter(card -> card.getCard().getCardType() == CardType.MONSTER));
        int index = MenuUtils.readCardByIndex(monsters.size());
        if (index == -1)
            return;
        board.sendToGraveyard(thisCard);
        PlayableCard toSummon = new PlayableCard(monsters.get(index).getCard(), CardPlaceType.MONSTER);
        toSummon.makeVisible();
        toSummon.setAttacking();
        board.addMonsterCard(toSummon);
    }

    public static boolean activateTrap(GameRoundController roundController, String[] cards, PlayableCard rivalCard) {
        System.out.println("Do you want to activate your trap or spell? (y/n)");
        if (!MenuUtils.readLine().equals("y"))
            return false;
        int index = DuelMenuUtils.printAndGetListOfCardToChooseWithCancel(cards);
        if (index == -1)
            return false;
        TrapCard card = TrapCard.getTrapCardByName(cards[index]);
        card.activateEffect(roundController.getRivalBoard(), roundController.getPlayerBoard(), null, rivalCard, 0);
        if (card instanceof NegateAttack) {
            try {
                roundController.advancePhase();
                System.out.println("Next phase!");
            } catch (PlayerTimeSealedException e) {
                throw new BooAnException(e.getMessage());
            }
        }
        return true;
    }

    public static void getMindCrushCard(GameRoundController gameRoundController, PlayableCard thisCard) {
        while (true) {
            System.out.println("Choose a card by it's name.");
            Card card = Card.getCardByName(MenuUtils.readLine());
            if (card == null) {
                System.out.println("This card does not exists!");
                continue;
            }
            MindCrush.getInstance().activateEffect(gameRoundController.getPlayerBoard(), gameRoundController.getRivalBoard(), null, new PlayableCard(card, CardPlaceType.HAND), 0);
            gameRoundController.getPlayerBoard().sendToGraveyard(thisCard);
            break;
        }
    }

    public static void handleTwinTwisters(GameRoundController round, PlayableCard thisCard) {
        ArrayList<PlayableCard> cards = round.getRivalBoard().getSpellCardsList();
        DuelMenuUtils.printNumberedCardList(cards);
        int index = MenuUtils.readCardByIndex(cards.size());
        if (index == -1)
            return;
        round.getRivalBoard().sendToGraveyard(cards.get(index));
        round.getPlayerBoard().sendToGraveyard(thisCard);
    }

    public static void handleMysticalSpaceTyphoon(GameRoundController roundController, PlayableCard thisCard) {
        ArrayList<PlayableCard> cards = roundController.getRivalBoard().getSpellCardsList();
        DuelMenuUtils.printNumberedCardList(cards);
        System.out.println("Select two cards (use cancel to dont select a card)");
        int index1 = MenuUtils.readCardByIndex(cards.size());
        int index2 = MenuUtils.readCardByIndex(cards.size());
        if (index1 != -1)
            roundController.getRivalBoard().sendToGraveyard(cards.get(index1));
        if (index1 != index2 && index2 != -1)
            roundController.getRivalBoard().sendToGraveyard(cards.get(index1));
        roundController.getPlayerBoard().getHand().remove(GameUtils.random.nextInt(roundController.getPlayerBoard().getHand().size()));
        roundController.getPlayerBoard().sendToGraveyard(thisCard);
    }
}
