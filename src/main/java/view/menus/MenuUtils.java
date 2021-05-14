package view.menus;

import model.cards.Card;

import java.util.Scanner;

class MenuUtils {
    static final String INVALID_COMMAND = "invalid command";
    static final String INVALID_NUMBER = "invalid number";
    static final String MENU_NAV_FAILED = "menu navigation is not possible";
    static final String CHOOSE_CARD_BY_INDEX = "Choose a card by it's index: ";
    static final String CANCEL_COMMAND = "cancel";
    private static final Scanner scanner = new Scanner(System.in);
    private static final String CARD_SHOW_PREFIX = "card show";

    static String readLine() {
        return scanner.nextLine().trim();
    }

    static boolean showCard(String command) {
        if (!command.startsWith(CARD_SHOW_PREFIX))
            return false;
        Card card = Card.getCardByName(command.substring(CARD_SHOW_PREFIX.length()));
        if (card == null)
            System.out.println("Card does not exists");
        else
            System.out.println(card);
        return true;
    }

    /**
     * Gets an index from user and returns the index
     *
     * @param size The max index allowed (exclusive). Just pass the array size
     * @return The index chosen. -1 if canceled
     */
    static int readCardByIndex(int size) {
        int index;
        while (true) {
            System.out.print(MenuUtils.CHOOSE_CARD_BY_INDEX);
            try {
                String command = readLine();
                if (command.equals(CANCEL_COMMAND))
                    return -1;
                index = Integer.parseInt(command) - 1;
                if (index < 0 || index >= size)
                    System.out.println("Out of range!");
                else
                    break;
            } catch (NumberFormatException ex) {
                System.out.println(MenuUtils.INVALID_NUMBER);
            }
        }
        return index;
    }
}
