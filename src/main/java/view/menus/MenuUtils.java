package view.menus;

import model.cards.Card;

import java.util.Scanner;

class MenuUtils {
    static final String INVALID_COMMAND = "invalid command";
    static final String INVALID_NUMBER = "invalid number";
    static final String MENU_NAV_FAILED = "menu navigation is not possible";
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
}
