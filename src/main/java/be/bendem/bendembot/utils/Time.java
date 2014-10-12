package be.bendem.bendembot.utils;

/**
 * @author bendem
 */
public class Time {

    public static long since(long since) {
        return now() - since;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

}
