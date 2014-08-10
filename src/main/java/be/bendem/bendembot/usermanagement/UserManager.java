package be.bendem.bendembot.usermanagement;

import be.bendem.bendembot.custompackets.KickIrcPacket;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.EventManager;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.event.EventHandler;
import fr.ribesg.alix.api.event.UserJoinChannelEvent;
import fr.ribesg.alix.api.message.ModeIrcPacket;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO Discover current users when joining a channel
 *
 * @author bendem
 */
public class UserManager {

    private final Map<String, User> users;

    public UserManager() {
        this.users = new ConcurrentHashMap<>();

        EventManager.register(this);
    }

    // TODO Move to another class maybe with some other hooks
    @EventHandler
    public void onJoin(UserJoinChannelEvent e) {
        if(!users.containsKey(e.getUser().getName())) {
            Log.info("Adding " + e.getUser().getName() + "!" + e.getUser().getUserName() + " to known list");
            users.put(e.getUser().getName(), new User(e.getUser()));
        }
    }

    public boolean isKnown(String nick) {
        return users.containsKey(nick);
    }

    public void kick(Channel channel, Source user, String message) {
        Validate.isTrue(user.isUser(), "Should be a user");
        channel.getServer().send(new KickIrcPacket(channel, user, message));
    }

    public void ban(Channel channel, Source user, String message) {
        Validate.isTrue(user.isUser(), "Should be a user");
        channel.getServer().send(new ModeIrcPacket(user.getHostName(), "+b", message));
    }

}
