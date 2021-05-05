package view.menus;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Setuper {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    public static void setup() {
        System.setOut(new PrintStream(outContent));
    }

    public static void restore() {
        System.setOut(originalOut);
    }

    public static void reset() {
        outContent.reset();
    }

    public static ByteArrayOutputStream getOutput() {
        return outContent;
    }
}
