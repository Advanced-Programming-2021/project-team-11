package view.menus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class Setuper {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final InputStream originalIn = System.in;

    public static void setup() {
        System.setOut(new PrintStream(outContent));
    }

    public static void restore() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    public static void reset() {
        outContent.reset();
        System.setIn(originalIn);
    }

    public static void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    public static String getOutputString() {
        return outContent.toString().replaceAll("\r\n","\n");
    }
}
