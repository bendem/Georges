package be.bendem.bendembot.utils;

import java.io.InputStream;

/**
 * @author bendem
 */
public class ResourceUtils {

    public static InputStream getStream(String filename) {
        return ResourceUtils.class.getProtectionDomain().getClassLoader().getResourceAsStream(filename);
    }

}
