package be.bendem.bendembot.utils;

/**
 * @author bendem
 */
public class Time {

    public static final long MILLISECONDS_IN_SECOND = 1_000;
    public static final long MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * 60;

    public static long since(long since) {
        return now() - since;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

}
