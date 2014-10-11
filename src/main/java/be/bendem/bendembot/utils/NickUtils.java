package be.bendem.bendembot.utils;

import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class NickUtils {

    private static final char ANTI_HL_CHAR = '\u200B';
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://.*");

    public static String antiHighlight(String text) {
        StringBuilder builder = new StringBuilder();
        for(String word : text.split(" ")) {
            builder
                .append(URL_PATTERN.matcher(word).matches() ? word : antiHighlightWord(word))
                .append(" ");
        }
        return builder.toString();
    }

    private static CharSequence antiHighlightWord(String word) {
        StringBuilder builder = new StringBuilder(word);
        for(int i = builder.length() - 1; i >= 0; i--) {
            builder.insert(i, ANTI_HL_CHAR);
        }
        return builder;
    }

}
