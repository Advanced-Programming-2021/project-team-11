package controller;

public class GeneralUtil {
    /**
     * Formats an enum name like "BRO_SUP" to "Bro Sup"
     *
     * @param name The name to format
     * @return Formatted name
     */
    public static String formatEnumName(String name) {
        name = name.replace('_', ' ');
        String[] words = name.split("\\s");
        StringBuilder formatted = new StringBuilder(name.length());
        for (String word : words) {
            formatted.append(word.substring(0, 1).toUpperCase());
            formatted.append(word.substring(1).toLowerCase());
            formatted.append(' ');
        }
        return formatted.toString().trim();
    }
}
