package be.bendem.bendembot.utils;

import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class StrUtils {

    private static final char ANTI_HL_CHAR = '\u200B';
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://.*");
    private static final Pattern COMMA_SPLITTER = Pattern.compile("\\s*,\\s*");

    /**
     * Inserts invisible characters between chars of a text to prevent it from
     * pinging users.
     * <p/>
     * Links and color codes are preserved by that method
     *
     * @param text the text to modify
     * @return the modified text
     */
    public static String antiPing(String text) {
        StringBuilder builder = new StringBuilder();
        for(String word : text.split(" ")) {
            builder
                // if it's an url or if it already contains a color code
                .append(isUrl(word) || word.contains(Character.toString((char) 0x03)) ? word : antiPingWord(word))
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

    /**
     * Checks wether the string given starts with "https?://"
     *
     * @param string the string to check
     * @return true if string matches "^https?://", false otherwise
     */
    public static boolean isUrl(String string) {
        return URL_PATTERN.matcher(string).matches();
    }

    /**
     * Pluralizes a string based on a given number
     *
     * @param count the number to check
     * @param singular the string to return if less than 2
     * @param plural the string to return if multiples
     * @return the correct form based on the given number
     */
    public static String plural(int count, String singular, String plural) {
        return count > 1 ? plural : singular;
    }

    /**
     * Splits a string built by joining the parts given with the glue given.
     *
     * @param iterable the parts of the string to join
     * @param glue the string to join the parts with
     * @return the parts of the string splitted by commas
     */
    public static String[] commaSplit(Iterable<String> iterable, String glue) {
        return commaSplit(String.join(glue, iterable));
    }

    /**
     * Splits a string by commas.
     *
     * @param string the string to split
     * @return the splitted string
     */
    public static String[] commaSplit(String string) {
        return COMMA_SPLITTER.split(string);
    }

    /**
     * Replaces all occurences of a String by another in a StringBuilder.
     *
     * @param builder the builder to replace from
     * @param search the String to replace
     * @param replace the String to use instead
     */
    public static void replace(StringBuilder builder, String search, String replace) {
        int index;
        do {
            index = builder.indexOf(search);
            builder.replace(index, search.length(), replace);
        } while (index >= 0);
    }

}
