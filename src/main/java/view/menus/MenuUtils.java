package view.menus;

import java.util.Scanner;

public class MenuUtils {
    public static final String INVALID_COMMAND = "invalid command";
    private static Scanner scanner = new Scanner(System.in);

    static String readLine() {
        return scanner.nextLine();
    }
}
