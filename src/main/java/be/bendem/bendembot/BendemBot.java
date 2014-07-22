package be.bendem.bendembot;

/**
 * @author bendem
 */
public class BendemBot {

    public static final String BUKKIT_URL = "http://jd.bukkit.org/beta/apidocs/";
    public static final String JAVA_URL = "http://docs.oracle.com/javase/7/docs/api/";
    public static final String APACHE_URL = "http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/";

    public static void main(final String args[]) {
        new IrcClient("BendemBot");
    }

}
