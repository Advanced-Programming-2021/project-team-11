package view.menus;

import java.util.Scanner;

public class MenuUtils {
    public static final String INVALID_COMMAND = "invalid command";
    public static final String MENU_NAV_FAILED = "menu navigation is not possible";
    private static final Scanner scanner = new Scanner(System.in);

    static String readLine() {
        return scanner.nextLine();
    }
}
