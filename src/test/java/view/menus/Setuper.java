package view.menus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Setuper {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    public static void setup() {
        System.setOut(new PrintStream(outContent));
    }

    public static void restore() {
        System.setOut(originalOut);
        MenuUtils.setScanner(System.in);
    }

    public static void reset() {
        outContent.reset();
    }

    public static void setInput(String input) {
        MenuUtils.setScanner(new ByteArrayInputStream(input.getBytes()));
    }

    public static String getOutputString() {
        return outContent.toString().replaceAll("\r\n", "\n");
    }
}
