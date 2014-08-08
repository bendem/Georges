package be.bendem.bendembot.utils;

/**
 * @author bendem
 */
public class NickUtils {

    private static final char ANTI_HL_CHAR = '\u200B';

    public static String antiHighlight(String text) {
        StringBuilder builder = new StringBuilder(text);
        for(int i = builder.length() - 1; i >= 0; i--) {
            builder.insert(i, ANTI_HL_CHAR);
        }
        return builder.toString();
    }

}
