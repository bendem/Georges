package be.bendem.bendembot.usermanagement;

import fr.ribesg.alix.api.Source;
import org.apache.commons.lang3.Validate;

/**
 * TODO This class contains informations about users (known nicks, userhost, username, reputation, etc)
 *
 * @author bendem
 */
public class User {

    private final String currentNick;
    private final String userName;
    private final String hostName;

    public User(Source user) {
        Validate.isTrue(user.isUser());
        currentNick = user.getName();
        userName = user.getUserName();
        hostName = user.getHostName();
    }

    public String getCurrentNick() {
        return currentNick;
    }

    public String getUserName() {
        return userName;
    }

    public String getHostName() {
        return hostName;
    }

}
