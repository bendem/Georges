package be.bendem.bendembot.usermanagement;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Source;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private final Set<String>       knownNicks;

    public User(Source user) {
        Validate.isTrue(user.isUser());
        currentNick = user.getName();
        userName = user.getUserName();
        hostName = user.getHostName();
        channels = new HashMap<>();
        for(Channel channel : user.getServer().getChannels()) {
            channel.getUserNicknames().stream()
                .filter(nick -> user.getName().equals(nick))
                .forEach(nick -> channels.put(channel.getName(), new Mode(channel.getName(), null)));
        }
        knownNicks = new HashSet<>();

        // TODO NickServ check
    }

    // TODO Remove that temporary thing
    public User(String currentNick) {
        this.hostName = null;
        this.userName = null;
        this.currentNick = currentNick;
        this.channels = new HashMap<>();
        this.knownNicks = new HashSet<>();
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

    public Set<String> getKnownNicks() {
        return knownNicks;
    }

}
