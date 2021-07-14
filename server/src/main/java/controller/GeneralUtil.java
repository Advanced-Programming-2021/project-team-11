package controller;

import java.util.Base64;

public class GeneralUtil {
    /**
     * Formats an enum name like "BRO_SUP" to "Bro Sup"
     *
     * @param name The name to format
     * @return Formatted name
     */
    public static String formatEnumName(String name) {
        String[] words = name.replace('_', ' ').split("\\s");
        StringBuilder formatted = new StringBuilder(name.length());
        for (String word : words) {
            formatted.append(word.substring(0, 1).toUpperCase());
            formatted.append(word.substring(1).toLowerCase());
            formatted.append(' ');
        }
        return formatted.toString().trim();
    }

    public static byte[] decodeFromBase64(String str) {
        if (str == null)
            return null;
        return Base64.getDecoder().decode(str);
    }

    public static String encodeToBase64(byte[] bytes) {
        if (bytes == null)
            return null;
        return Base64.getEncoder().encodeToString(bytes);
    }
}
