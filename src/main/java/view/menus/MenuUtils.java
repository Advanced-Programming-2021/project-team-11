package view.menus;

import java.util.Scanner;

class MenuUtils {
    static final String INVALID_COMMAND = "invalid command";
    static final String MENU_NAV_FAILED = "menu navigation is not possible";
    static final Scanner scanner = new Scanner(System.in);

    static String readLine() {
        return scanner.nextLine();
    }
}
