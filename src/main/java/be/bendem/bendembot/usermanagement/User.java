package be.bendem.bendembot.usermanagement;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Source;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO This class contains informations about users (known nicks, userhost, username, reputation, etc)
 *
 * @author bendem
 */
public class User {

    private final String            currentNick;
    private final String            userName;
    private final String            hostName;
    // Contains <channel, mode>
    private final Map<String, Mode> channels;

    public User(Source user) {
        Validate.isTrue(user.isUser());
        currentNick = user.getName();
        userName = user.getUserName();
        hostName = user.getHostName();
        channels = new HashMap<>();
        for(Channel channel : user.getServer().getChannels()) {
            for(String nick : channel.getUserNicknames()) {
                if(user.getName().equals(nick)) {
                    channels.put(channel.getName(), new Mode(channel.getName(), null));
                }
            }

        }

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
