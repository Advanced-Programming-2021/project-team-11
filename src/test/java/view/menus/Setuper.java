package view.menus;

import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
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

    public static void matchLines(String toMatchLines) {
        String[] lines = toMatchLines.split("\n");
        String[] outputLines = Setuper.getOutputString().split("\n");
        for (int i = 0; i < lines.length; i++)
            if (!outputLines[i].matches(lines[i]))
                Assertions.fail(outputLines[i] + " does not match " + lines[i]);
    }
}
