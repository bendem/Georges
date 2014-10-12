package be.bendem.bendembot.utils;

import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class StrUtils {

    private static final char ANTI_HL_CHAR = '\u200B';
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://.*");

    public static String antiPing(String text) {
        StringBuilder builder = new StringBuilder();
        for(String word : text.split(" ")) {
            builder
                .append(isUrl(word) ? word : antiPingWord(word))
                .append(" ");
        }
        return builder.toString();
    }

    private static CharSequence antiPingWord(String word) {
        StringBuilder builder = new StringBuilder(word);
        for(int i = builder.length() - 1; i >= 0; i--) {
            builder.insert(i, ANTI_HL_CHAR);
        }
        return builder;
    }

    public static boolean isUrl(String string) {
        return URL_PATTERN.matcher(string).matches();
    }

    public static String plural(int count, String singular, String plural) {
        return count > 1 ? plural : singular;
    }

}
