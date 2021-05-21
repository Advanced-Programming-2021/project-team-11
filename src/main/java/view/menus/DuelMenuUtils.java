package view.menus;

import model.PlayableCard;
import model.PlayerBoard;
import model.User;
import model.cards.Card;
import model.cards.MonsterCard;
import model.exceptions.BooAnException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DuelMenuUtils {
    public static final int[] RIVAL_BOARD_INDEXES = {5, 3, 1, 2, 4}, MY_BOARD_INDEXES = {4, 2, 1, 3, 5};

    public static void printRivalBoard(PlayerBoard board) {
        // Count cards in hand
        board.getHand().forEach(x -> System.out.print("\tc"));
        System.out.println();
        // Print deck
        System.out.printf("%02d\n", board.getDeck().size());
        // Spell and traps
        Arrays.stream(RIVAL_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getSpellCards()[i - 1] == null ? "E " : board.getSpellCards()[i - 1].toString())));
        System.out.println();
        Arrays.stream(RIVAL_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getMonsterCards()[i - 1] == null ? "E " : board.getMonsterCards()[i - 1].toString())));
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
        Arrays.stream(MY_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getMonsterCards()[i - 1] == null ? "E " : board.getMonsterCards()[i - 1].toString())));
        System.out.println();
        Arrays.stream(RIVAL_BOARD_INDEXES).forEach(i -> System.out.print("\t" + (board.getSpellCards()[i - 1] == null ? "E " : board.getSpellCards()[i - 1].toString())));
        System.out.println();
        // Print deck
        for (int i = 0; i < 6; i++)
            System.out.print('\t');
        System.out.printf("%02d\n", board.getDeck().size());
        // Count cards in hand
        board.getHand().forEach(x -> System.out.print("c\t"));
        System.out.println();
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

    public static void printGraveyard(ArrayList<PlayableCard> graveyard, User player) {
        printGraveyard(graveyard, player.getNickname());
    }

    public static void printGraveyard(ArrayList<PlayableCard> graveyard, String playerName) {
        System.out.printf("%s's graveyard\n", playerName);
        if (graveyard.size() == 0)
            System.out.println("graveyard empty");
        else
            for (int i = 0; i < graveyard.size(); i++)
                System.out.printf("%d. %s:%s\n", i + 1, graveyard.get(i).getCard().getName(), graveyard.get(i).getCard().getDescription());
    }

    /**
     * Reads a amount of cards to tribute them
     *
     * @param neededCardsToTribute Number of cards needed to tribute
     * @return Null if canceled, otherwise the array of positions
     */
    public static ArrayList<Integer> readCardsToTribute(int neededCardsToTribute) {
        TreeSet<Integer> cardPositions = new TreeSet<>();
        while (cardPositions.size() != neededCardsToTribute) {
            System.out.printf("Select a card position to tribute or type \"cancel\" to cancel (%d/%d): ",
                    cardPositions.size(), neededCardsToTribute);
            String command = MenuUtils.readLine();
            if (command.equals(MenuUtils.CANCEL_COMMAND))
                return null;
            try {
                int position = Integer.parseInt(command);
                if (position > 0 && position <= 5)
                    cardPositions.add(position);
            } catch (NumberFormatException ignored) {
            }
        }
        return new ArrayList<>(cardPositions);
    }

    public static ArrayList<PlayableCard> printNumberedCardList(Stream<PlayableCard> cards) {
        ArrayList<PlayableCard> list = cards.collect(Collectors.toCollection(ArrayList::new));
        printNumberedCardList(list);
        return list;
    }

    public static void printNumberedCardList(ArrayList<PlayableCard> cards) {
        for (int i = 0; i < cards.size(); i++)
            System.out.printf("%d. %s\n", i + 1, cards.get(i).getCard().getName());
    }

    public static ArrayList<Card> printNumberedRawCardList(Stream<Card> cards) {
        ArrayList<Card> list = cards.collect(Collectors.toCollection(ArrayList::new));
        printNumberedRawCardList(list);
        return list;
    }

    public static void printNumberedRawCardList(ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++)
            System.out.printf("%d. %s\n", i + 1, cards.get(i).getName());
    }

    /**
     * Prints a numbered list of cards and their levels
     *
     * @param cards List of cards
     */
    public static void printNumberedCardListWithLevel(ArrayList<PlayableCard> cards) {
        for (int i = 0; i < cards.size(); i++)
            System.out.printf("%d. %s -> level %d\n", i + 1, cards.get(i).getCard().getName(), ((MonsterCard) cards.get(i).getCard()).getLevel());
    }

    /**
     * Prints a numbered list of cards with the last element being "cancel the activation"
     *
     * @param cards The list of cards
     * @return The index in the array if a card is selected. Returns -1 if canceled
     */
    public static int printAndGetListOfCardToChooseWithCancel(String[] cards) {
        System.out.println("Select a card:");
        for (int i = 0; i < cards.length; i++)
            System.out.printf("%d. %s\n", i + 1, cards[i]);
        System.out.printf("%d. %s\n", cards.length + 1, "cancel the activation");
        int index = MenuUtils.readCardByIndex(cards.length + 1);
        if (index == -1 || index == cards.length + 1)
            return -1;
        return index;
    }
}
