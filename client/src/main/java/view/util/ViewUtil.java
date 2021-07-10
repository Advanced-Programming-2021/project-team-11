package view.util;

public class ViewUtil {
    /**
     * This function turns long string to small strings which contain '...' at last of them
     *
     * @param string The string to trim
     * @param size   The max size of string
     * @return The formatted string
     */
    public static String beautyPrintText(String string, int size) {
        if (string.length() <= size)
            return string;
        return string.substring(0, size - 3) + "...";
    }
}
